package com.thetechmaddy.ecommerce.services.impl;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.exceptions.DuplicatePendingOrderException;
import com.thetechmaddy.ecommerce.exceptions.EmptyCartException;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import com.thetechmaddy.ecommerce.models.calculators.GrossTotalCalculator;
import com.thetechmaddy.ecommerce.models.calculators.NetTotalCalculator;
import com.thetechmaddy.ecommerce.models.calculators.OrderTotalCalculator;
import com.thetechmaddy.ecommerce.models.mappers.CartItemToOrderItemMapper;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.repositories.OrdersRepository;
import com.thetechmaddy.ecommerce.services.CartLockApplierService;
import com.thetechmaddy.ecommerce.services.CartsService;
import com.thetechmaddy.ecommerce.services.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.thetechmaddy.ecommerce.models.OrderItemStatus.PENDING_ORDER_CONFIRMATION;
import static com.thetechmaddy.ecommerce.models.OrderStatus.PENDING;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CASH_ON_DELIVERY;

@Log4j2
@Primary
@Service
@Qualifier("ordersServiceImpl")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrdersServiceImpl implements OrdersService {

    private final CartsService cartsService;
    private final OrdersRepository ordersRepository;
    private final CartLockApplierService cartLockApplierService;

    private final CartItemToOrderItemMapper cartItemToOrderItemMapper;

    @Override
    public Order initiateOrder(String userId, OrderRequest orderRequest) {
        List<Order> userOrders = ordersRepository.findByUserIdAndStatusEquals(userId, PENDING);

        if (userOrders.isEmpty()) {
            return createNewOrder(userId, orderRequest);
        }

        if (userOrders.size() == 1) {
            Order order = userOrders.get(0);
            ordersRepository.updateTime(order.getId());
            return order;
        }

        throw new DuplicatePendingOrderException(String.format("Found more than one PENDING order for user: (userId - %s)", userId));
    }

    @Override
    @Transactional
    public Order createNewOrder(String userId, OrderRequest orderRequest) {
        ensureNoPendingOrderExists(userId);

        Cart cart = cartsService.getCart(orderRequest.getCartId(), userId);

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new EmptyCartException(String.format("Can not create order: cart is empty: (cartId - %d)", cart.getId()));
        }

        cartLockApplierService.acquireLock(cart);

        Order.OrderBuilder builder = Order.builder();

        builder.userId(userId);

        PaymentInfo paymentInfo = orderRequest.getPaymentInfo();
        boolean cashOnDeliveryMode = CASH_ON_DELIVERY == paymentInfo.getPaymentMode();
        builder.status(cashOnDeliveryMode ? OrderStatus.CONFIRMED : PENDING);

        OrderTotalCalculator netTotalCalculator = new NetTotalCalculator();
        OrderTotalCalculator grossTotalCalculator = new GrossTotalCalculator();

        builder.netTotal(netTotalCalculator.calculate(cartItems));
        builder.grossTotal(grossTotalCalculator.calculate(cartItems));

        Order order = builder.build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItemToOrderItemMapper::cartItemToOrderItem)
                .peek(orderItem -> {
                    orderItem.setStatus(cashOnDeliveryMode ? OrderItemStatus.CONFIRMED : PENDING_ORDER_CONFIRMATION);
                    orderItem.setOrder(order);
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        log.info(String.format("Creating new order for user: (userId - %s)", userId));

        Order newOrder = ordersRepository.save(order);

        if (cashOnDeliveryMode) {
            cartLockApplierService.releaseLock(cart);
        }
        return newOrder;
    }

    private void ensureNoPendingOrderExists(String userId) {
        long totalPendingOrders = ordersRepository.countByUserIdAndStatusEquals(userId, PENDING);

        if (totalPendingOrders > 0) {
            throw new DuplicatePendingOrderException(String.format("Found existing PENDING order for user: (userId - %s)", userId));
        }
    }
}
