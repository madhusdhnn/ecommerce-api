package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.calculators.GrossTotalCalculator;
import com.thetechmaddy.ecommerce.models.calculators.NetTotalCalculator;
import com.thetechmaddy.ecommerce.models.mappers.CartItemToOrderItemMapper;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.repositories.OrderItemsRepository;
import com.thetechmaddy.ecommerce.repositories.OrdersRepository;
import com.thetechmaddy.ecommerce.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.thetechmaddy.ecommerce.models.OrderItemStatus.PENDING_ORDER_CONFIRMATION;
import static com.thetechmaddy.ecommerce.models.OrderStatus.CONFIRMED;
import static com.thetechmaddy.ecommerce.models.OrderStatus.PENDING;

@Log4j2
@Primary
@Service
@Qualifier("ordersServiceImpl")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrdersServiceImpl implements OrdersService {

    private final CartsService cartsService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final DeliveryDetailsService deliveryDetailsService;
    private final CartLockApplierService cartLockApplierService;

    private final CartItemToOrderItemMapper cartItemToOrderItemMapper;

    @Override
    public Order initiateOrder(String userId, OrderRequest orderRequest) {
        Order order = getOrderInPendingStatus(userId);

        if (order == null) {
            order = createNewOrder(userId, orderRequest);
        }

        return order;
    }

    @Override
    @Transactional
    public Order createNewOrder(String userId, OrderRequest orderRequest) {
        ensureNoPendingOrderExists(userId);

        Cart cart = cartsService.getCart(orderRequest.getCartId(), userId);

        List<CartItem> cartItems = cart.getCartItems();
        ensureCartNotEmpty(cart.getId(), cartItems);

        cartLockApplierService.acquireLock(cart);

        PaymentInfo paymentInfo = orderRequest.getPaymentInfo();
        Order newOrder = createOrder(userId, paymentInfo, cartItems);

        DeliveryDetails deliveryDetails = deliveryDetailsService.saveDeliveryInfo(orderRequest.getDeliveryInfo(), newOrder);
        newOrder.setDeliveryDetails(deliveryDetails);
        log.info(String.format("Saved delivery information for order: (orderId - %d) and user: (userId - %s)", newOrder.getId(), userId));

        Payment newPayment = paymentService.savePaymentInfo(paymentInfo, newOrder);
        newOrder.setPayment(newPayment);
        log.info(String.format("Saved payment information for order: (orderId - %d) and user: (userId - %s)", newOrder.getId(), userId));

        if (paymentInfo.isCashOnDeliveryMode()) {
            cartLockApplierService.releaseLock(cart);
            cartsService.clearCart(cart.getId(), userId);

            updateProductsStock(cart.getCartItems());
        }

        return newOrder;
    }

    @Override
    public Order placeOrder(long orderId, CognitoUser customer) {
        Order order = this.ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.isPending()) {
            throw new UnprocessableOrderException(String.format("Order: (orderId - %d) not in pending status", orderId));
        }

        Payment payment = order.getPayment();

        if (payment.isPending()) {
            throw new PaymentRequiredException(
                    String.format("Payment: (paymentId - %d) is not completed for the order: (orderId - %d)",
                            payment.getId(), order.getId())
            );
        }

        if (!payment.isSuccess()) {
            throw new PaymentNotInSuccessException(
                    String.format("Payment: (paymentId - %d) not in success status for the order: (orderId - %d)",
                            payment.getId(), order.getId())
            );
        }

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

        Cart cart = cartsService.getUserCart(customer.getCognitoSub());

        cartLockApplierService.releaseLock(cart);
        cartsService.clearCart(cart.getId(), customer.getCognitoSub());

        updateProductsStock(cart.getCartItems());

        return order;
    }

    private Order createOrder(String userId, PaymentInfo paymentInfo, List<CartItem> cartItems) {
        Order.OrderBuilder builder = Order.builder();

        builder.userId(userId);

        builder.status(paymentInfo.isCashOnDeliveryMode() ? CONFIRMED : PENDING);

        NetTotalCalculator netTotalCalculator = new NetTotalCalculator();
        GrossTotalCalculator grossTotalCalculator = new GrossTotalCalculator();

        builder.netTotal(netTotalCalculator.calculate(cartItems));
        builder.grossTotal(grossTotalCalculator.calculate(cartItems));

        Order order = builder.build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItemToOrderItemMapper::mapCartItemToOrderItem)
                .peek(orderItem -> {
                    orderItem.setStatus(getOrderItemStatus(paymentInfo.isCashOnDeliveryMode()));
                    orderItem.setOrder(order);
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order newOrder = ordersRepository.save(order);
        log.info(String.format("Created new order for user: (userId - %s)", userId));

        return newOrder;
    }

    private Order getOrderInPendingStatus(String userId) {
        List<Order> userOrders = ordersRepository.findByUserIdAndStatusEquals(userId, PENDING);

        if (userOrders.isEmpty()) {
            return null;
        }

        if (userOrders.size() == 1) {
            Order order = userOrders.get(0);
            ordersRepository.updateTime(order.getId());
            return order;
        }

        throw new DuplicatePendingOrderException(String.format("Found more than one PENDING order for user: (userId - %s)", userId));
    }

    private static void ensureCartNotEmpty(long cartId, List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new EmptyCartException(String.format("Can not create order: cart is empty: (cartId - %d)", cartId));
        }
    }

    private void ensureNoPendingOrderExists(String userId) {
        long totalPendingOrders = ordersRepository.countByUserIdAndStatusEquals(userId, PENDING);

        if (totalPendingOrders > 0) {
            throw new DuplicatePendingOrderException(String.format("Found existing PENDING order for user: (userId - %s)", userId));
        }
    }

    private static OrderItemStatus getOrderItemStatus(boolean cashOnDeliveryMode) {
        return cashOnDeliveryMode ? OrderItemStatus.CONFIRMED : PENDING_ORDER_CONFIRMATION;
    }

    private void updateProductsStock(List<CartItem> cartItems) {
        Map<Long, Integer> productIdQuantityMap = cartItems.stream()
                .collect(Collectors.toMap(cartItem -> cartItem.getProduct().getId(), CartItem::getQuantity));
        inventoryService.updateProductsStock(productIdQuantityMap);
    }
}
