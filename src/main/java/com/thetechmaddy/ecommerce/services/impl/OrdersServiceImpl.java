package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.Address;
import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.exceptions.CartNotLockedException;
import com.thetechmaddy.ecommerce.exceptions.DuplicatePendingOrderException;
import com.thetechmaddy.ecommerce.exceptions.OrderItemsTotalMismatchException;
import com.thetechmaddy.ecommerce.exceptions.OrderNotFoundException;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.calculators.GrossTotalCalculator;
import com.thetechmaddy.ecommerce.models.calculators.NetTotalCalculator;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.mappers.CartItemToOrderItemMapper;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.repositories.OrderItemsRepository;
import com.thetechmaddy.ecommerce.repositories.OrdersRepository;
import com.thetechmaddy.ecommerce.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.thetechmaddy.ecommerce.models.OrderItemStatus.PENDING_ORDER_CONFIRMATION;
import static com.thetechmaddy.ecommerce.models.OrderStatus.CONFIRMED;
import static com.thetechmaddy.ecommerce.models.OrderStatus.PENDING;
import static com.thetechmaddy.ecommerce.utils.CartUtils.ensureCartNotEmpty;
import static com.thetechmaddy.ecommerce.utils.CartUtils.ensureCartTotalAndPaymentMatches;
import static com.thetechmaddy.ecommerce.utils.OrderUtils.ensureOrderInPendingStatus;
import static com.thetechmaddy.ecommerce.utils.PaymentUtils.ensurePaymentInEditableState;
import static com.thetechmaddy.ecommerce.utils.PaymentUtils.ensurePaymentIsSuccess;

@Log4j2
@Primary
@Service
@Qualifier("ordersServiceImpl")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrdersServiceImpl implements OrdersService {

    private final CartsService cartsService;
    private final PaymentService paymentService;
    private final ProductsService productsService;
    private final DeliveryDetailsService deliveryDetailsService;
    private final CartLockApplierService cartLockApplierService;

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;

    private final CartItemToOrderItemMapper cartItemToOrderItemMapper;

    @Override
    public Order getUserOrderInPendingStatus(String userId) {
        List<Order> userOrders = ordersRepository.findByUserIdAndStatusEquals(userId, PENDING);

        if (userOrders.isEmpty()) {
            return null;
        }

        if (userOrders.size() == 1) {
            return userOrders.get(0);
        }

        throw new DuplicatePendingOrderException(String.format("Found more than one PENDING order for user: (userId - %s)", userId));
    }

    @Override
    @Transactional
    public Order createNewOrder(String userId, OrderRequest orderRequest) {
        ensureNoPendingOrderExists(userId);

        Cart cart = cartsService.getCart(orderRequest.getCartId(), userId);
        ensureCartLocked(cart);

        List<CartItem> cartItems = cart.getCartItems();
        ensureCartNotEmpty(cart.getId(), cartItems);

        PaymentInfo paymentInfo = orderRequest.getPaymentInfo();
        ensureCartTotalAndPaymentMatches(paymentInfo, cart);

        Order newOrder = createOrder(userId, paymentInfo, cart.getCartItems());

        deliveryDetailsService.saveDeliveryInfo(orderRequest.getDeliveryInfo(), newOrder);
        log.info(String.format("Saved delivery information for order: (orderId - %d) and user: (userId - %s)", newOrder.getId(), userId));

        paymentService.savePaymentInfo(paymentInfo, newOrder);
        log.info(String.format("Saved payment information for order: (orderId - %d) and user: (userId - %s)", newOrder.getId(), userId));

        if (paymentInfo.isCashOnDelivery()) {
            String unlockReleaseReason = String.format("Order: (orderId - %d) confirmed as it is a CASH_ON_DELIVERY for user: (userId - %s)",
                    newOrder.getId(), userId);
            unlockAndClearCart(cart, userId, unlockReleaseReason);
            reserveProducts(cart.getCartItems());
        }

        return newOrder;
    }

    @Override
    @Transactional
    public Order placeOrder(long orderId, CognitoUser customer) {
        Order order = this.ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        ensureOrderInPendingStatus(order);
        ensurePaymentIsSuccess(order.getPayment());

        Cart cart = cartsService.getUserCart(customer.getCognitoSub());
        reserveProducts(cart.getCartItems());

        order.setStatus(CONFIRMED);
        log.info(String.format("Order: (orderId - %d) is confirmed for user: (userId - %s)", order.getId(), customer.getCognitoSub()));

        List<OrderItem> orderItems = order.getOrderItems()
                .stream()
                .peek(oi -> {
                    oi.setStatus(OrderItemStatus.CONFIRMED);
                    oi.setOrder(order);
                })
                .collect(Collectors.toList());
        orderItemsRepository.saveAll(orderItems);

        String unlockReleaseReason = String.format("Order: (orderId - %d) confirmed for user: (userId - %s)",
                orderId, customer.getCognitoSub());
        unlockAndClearCart(cart, customer.getCognitoSub(), unlockReleaseReason);

        return order;
    }

    @Override
    public Order getOrder(long orderId, String userId) {
        return ordersRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public Paged<Order> getUserOrders(Integer page, Integer size, String userId) {
        Page<Order> pagedOrders = ordersRepository.findAll(PageRequest.of(page, size));
        return new Paged<>(pagedOrders.getContent(), page, pagedOrders.getTotalElements(), pagedOrders.getNumberOfElements());
    }

    @Override
    @Transactional
    public void updatePaymentInfo(long orderId, String userId, PaymentInfo paymentInfo) {
        Order order = getOrder(orderId, userId);
        ensureOrderInPendingStatus(order);

        if (order.getGrossTotal().compareTo(paymentInfo.getAmount()) != 0) {
            throw new OrderItemsTotalMismatchException(
                    String.format("Payment update request amount: (%s) and order total amount: (%s) do not match.",
                            paymentInfo.getAmount(), order.getGrossTotal()));
        }

        Payment payment;
        if ((payment = order.getPayment()) != null) {
            ensurePaymentInEditableState(payment);

            payment.setPaymentMode(paymentInfo.getPaymentMode());
            payment.setAmount(paymentInfo.getAmount());
            payment.setStatus(PaymentStatus.PENDING);

            ordersRepository.save(order);
        }
    }

    @Override
    @Transactional
    public void updateDeliveryInfo(long orderId, String userId, DeliveryInfo deliveryInfo) {
        Order order = getOrder(orderId, userId);
        ensureOrderInPendingStatus(order);

        DeliveryDetails deliveryDetails;
        if ((deliveryDetails = order.getDeliveryDetails()) != null) {
            mergeDeliveryInfo(deliveryDetails, deliveryInfo);
            ordersRepository.save(order);
        }
    }

    @Override
    @Transactional
    public void deleteDraftOrder(long orderId, String userId) {
        Order order = getOrder(orderId, userId);
        ensureOrderInPendingStatus(order);
        ordersRepository.delete(order);

        Cart cart = cartsService.getUserCart(userId);
        Map<Long, Integer> productIdQuantityMap = cart.getCartItems().stream()
                .collect(Collectors.toMap(cartItem -> cartItem.getProduct().getId(), CartItem::getQuantity));
        productsService.restoreProducts(productIdQuantityMap);

        String unlockReleaseReason = String.format("Draft order: (orderId - %d) deleted by user: (userId - %s)", orderId, userId);
        cartLockApplierService.releaseLock(cart, unlockReleaseReason);
    }

    private void unlockAndClearCart(Cart cart, String userId, String unlockReleaseReason) {
        cartLockApplierService.releaseLock(cart, unlockReleaseReason);
        cartsService.clearCart(cart.getId(), userId);
    }

    private static void ensureCartLocked(Cart cart) {
        Objects.requireNonNull(cart, "cart == null");
        if (cart.isUnlocked()) {
            throw new CartNotLockedException(String.format("Cart: (cartId - %d) not locked", cart.getId()));
        }
    }

    private Order createOrder(String userId, PaymentInfo paymentInfo, List<CartItem> cartItems) {
        Order.OrderBuilder builder = Order.builder();

        builder.userId(userId);
        builder.status(paymentInfo.isCashOnDelivery() ? CONFIRMED : PENDING);

        NetTotalCalculator netTotalCalculator = new NetTotalCalculator();
        GrossTotalCalculator grossTotalCalculator = new GrossTotalCalculator();

        builder.netTotal(netTotalCalculator.calculate(cartItems));
        builder.grossTotal(grossTotalCalculator.calculate(cartItems));

        Order order = builder.build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItemToOrderItemMapper::mapCartItemToOrderItem)
                .peek(orderItem -> {
                    orderItem.setStatus(getOrderItemStatus(paymentInfo.isCashOnDelivery()));
                    orderItem.setOrder(order);
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order newOrder = ordersRepository.save(order);
        log.info(String.format("Created new order for user: (userId - %s)", userId));

        return newOrder;
    }

    private void ensureNoPendingOrderExists(String userId) {
        long totalPendingOrders = ordersRepository.countByUserIdAndStatusEquals(userId, PENDING);

        if (totalPendingOrders > 0) {
            throw new DuplicatePendingOrderException(String.format("Found existing PENDING order for user: (userId - %s)", userId));
        }
    }

    private void reserveProducts(List<CartItem> cartItems) {
        Map<Long, Integer> productIdQuantityMap = cartItems.stream()
                .collect(Collectors.toMap(cartItem -> cartItem.getProduct().getId(), CartItem::getQuantity));
        productsService.reserveProducts(productIdQuantityMap);
    }

    private static OrderItemStatus getOrderItemStatus(boolean cashOnDeliveryMode) {
        return cashOnDeliveryMode ? OrderItemStatus.CONFIRMED : PENDING_ORDER_CONFIRMATION;
    }

    private static void mergeDeliveryInfo(DeliveryDetails deliveryDetails, DeliveryInfo deliveryInfo) {
        Objects.requireNonNull(deliveryDetails);
        Objects.requireNonNull(deliveryInfo);

        deliveryDetails.setCustomerName(deliveryInfo.getCustomer().getName());
        deliveryDetails.setCustomerEmail(deliveryInfo.getCustomer().getEmail());

        Address shippingAddress = deliveryInfo.getShippingAddress();
        deliveryDetails.setShippingAddressOne(shippingAddress.getAddressOne());
        deliveryDetails.setShippingAddressTwo(shippingAddress.getAddressTwo());
        deliveryDetails.setShippingCity(shippingAddress.getCity());
        deliveryDetails.setShippingState(shippingAddress.getState());
        deliveryDetails.setShippingZipCode(shippingAddress.getZipCode());

        Address billingAddress = deliveryInfo.getBillingAddress();
        deliveryDetails.setBillingAddressOne(billingAddress.getAddressOne());
        deliveryDetails.setBillingAddressTwo(billingAddress.getAddressTwo());
        deliveryDetails.setBillingCity(billingAddress.getCity());
        deliveryDetails.setBillingState(billingAddress.getState());
        deliveryDetails.setBillingZipCode(billingAddress.getZipCode());
    }
}
