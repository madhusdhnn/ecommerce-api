package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.Address;
import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.CartNotBelongsToUserException;
import com.thetechmaddy.ecommerce.exceptions.CartNotFoundException;
import com.thetechmaddy.ecommerce.exceptions.DuplicatePendingOrderException;
import com.thetechmaddy.ecommerce.exceptions.EmptyCartException;
import com.thetechmaddy.ecommerce.models.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.repositories.CartItemsRepository;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.repositories.OrdersRepository;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static com.thetechmaddy.ecommerce.models.OrderStatus.CONFIRMED;
import static com.thetechmaddy.ecommerce.models.OrderStatus.PENDING;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CASH_ON_DELIVERY;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CREDIT_CARD;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrdersServiceTest extends BaseIntegrationTest {

    private static final String TEST_COGNITO_SUB = "test-cognito-sub";

    @Autowired
    @Qualifier("ordersServiceImpl")
    private OrdersService ordersService;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    private long cartId;

    @BeforeAll
    public void setupCart() {
        cartsRepository.deleteAll();

        Cart cart = cartsRepository.save(new Cart(TEST_COGNITO_SUB, UN_LOCKED));
        cartId = cart.getId();
    }

    @BeforeEach
    public void resetOrders() {
        ordersRepository.deleteAll();
        ordersRepository.flush();
    }

    @Test
    public void testNewOrderInitiateCartNotFoundException() {
        assertThrows(CartNotFoundException.class, () ->
                ordersService.initiateOrder(TEST_COGNITO_SUB,
                        getOrderRequest(Integer.MAX_VALUE, TestOrderRequestOptions.builder().build())));
    }

    @Test
    public void testNewOrderInitiateCartNotBelongsToUserException() {
        assertThrows(CartNotBelongsToUserException.class, () ->
                ordersService.initiateOrder("some-other-user",
                        getOrderRequest(cartId, TestOrderRequestOptions.builder().build())));
    }

    @Test
    @Transactional
    public void testNewOrderInitiateCartIsEmptyException() {
        assertThrows(EmptyCartException.class, () ->
                ordersService.initiateOrder(TEST_COGNITO_SUB,
                        getOrderRequest(cartId, TestOrderRequestOptions.builder().build())));
    }

    @Test
    @Transactional
    public void testNewOrderInitiateCashOnDelivery() {
        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .paymentMode(CASH_ON_DELIVERY)
                .build();

        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        Order order = ordersService.initiateOrder(TEST_COGNITO_SUB, getOrderRequest(cartId, options));
        assertNotNull(order);

        assertEquals(CONFIRMED, order.getStatus());
        order.getOrderItems().forEach(oi -> assertEquals(OrderItemStatus.CONFIRMED, oi.getStatus()));

        BigDecimal netTotal = order.getOrderItems().stream()
                .map(OrderItem::getNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(netTotal, order.getNetTotal());

        BigDecimal grossTotal = order.getOrderItems().stream()
                .map(OrderItem::getGrossAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(grossTotal, order.getGrossTotal());

        DeliveryDetails deliveryDetails = order.getDeliveryDetails();
        assertNotNull(deliveryDetails);

        Payment payment = order.getPayment();
        assertNotNull(payment);
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        assertTrue(cartsRepository.isUnlocked(cartId, TEST_COGNITO_SUB));
        assertEquals(0, cartItemsRepository.countByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB));
    }

    @Test
    @Transactional
    public void testInitiateOrderReturnExisting() {
        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .paymentMode(CREDIT_CARD)
                .build();

        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        // Request for same order
        Order actual = ordersService.initiateOrder(TEST_COGNITO_SUB, orderRequest);
        assertEquals(order, actual);
    }

    @Test
    @Transactional
    public void testCreateOrderDuplicateError() {
        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .paymentMode(CREDIT_CARD)
                .build();

        getTestProducts().stream().filter(Product::isInStock).forEach(product ->
                cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId));

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        assertThrows(DuplicatePendingOrderException.class, () -> ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest));
    }

    @Test
    @Transactional
    public void testNewOrderInitiateOtherPaymentModes() {
        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .paymentMode(CREDIT_CARD)
                .build();

        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        Order order = ordersService.initiateOrder(TEST_COGNITO_SUB, getOrderRequest(cartId, options));
        assertNotNull(order);

        assertEquals(PENDING, order.getStatus());
        order.getOrderItems().forEach(oi -> assertEquals(OrderItemStatus.PENDING_ORDER_CONFIRMATION, oi.getStatus()));

        BigDecimal netTotal = order.getOrderItems().stream()
                .map(OrderItem::getNetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(netTotal, order.getNetTotal());

        BigDecimal grossTotal = order.getOrderItems().stream()
                .map(OrderItem::getGrossAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(grossTotal, order.getGrossTotal());

        DeliveryDetails deliveryDetails = order.getDeliveryDetails();
        assertNotNull(deliveryDetails);

        Payment payment = order.getPayment();
        assertNotNull(payment);
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        assertFalse(cartsRepository.isUnlocked(cartId, TEST_COGNITO_SUB));
    }

    @Test
    @Transactional
    public void testDuplicateOrderException() {
        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .paymentMode(CREDIT_CARD)
                .build();

        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        Order order2 = new Order(order);

        ordersRepository.saveAndFlush(order2);

        assertThrows(DuplicatePendingOrderException.class, () -> ordersService.initiateOrder(TEST_COGNITO_SUB, orderRequest));
    }

    private OrderRequest getOrderRequest(long cartId, TestOrderRequestOptions options) {
        DeliveryInfo deliveryInfo = null;
        PaymentInfo paymentInfo = null;

        if (options.isCreateDeliveryInfo()) {
            Address shippingAddress = Address.builder()
                    .addressOne("Wall St")
                    .zipCode("567234")
                    .build();

            Address billingAddress = Address.builder()
                    .addressOne("Journal St")
                    .zipCode("567244")
                    .build();

            deliveryInfo = new DeliveryInfo(
                    "Jane Doe",
                    "jane.doe@example.com",
                    options.isShippingSameAsBilling() ? billingAddress : shippingAddress,
                    billingAddress
            );
        }

        if (options.isCreatePaymentInfo()) {
            paymentInfo = new PaymentInfo(new BigDecimal("140500"), options.getPaymentMode());
        }

        return new OrderRequest(cartId, deliveryInfo, paymentInfo);
    }


    @Getter
    @Builder
    private static class TestOrderRequestOptions {
        private boolean createDeliveryInfo;
        private boolean shippingSameAsBilling;
        private boolean createPaymentInfo;
        private PaymentMode paymentMode;
    }

}
