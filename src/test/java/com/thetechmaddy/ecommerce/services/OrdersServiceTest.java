package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.repositories.CartItemsRepository;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.repositories.OrdersRepository;
import com.thetechmaddy.ecommerce.repositories.PaymentsRepository;
import com.thetechmaddy.ecommerce.utils.TestUtils.TestOrderRequestOptions;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static com.thetechmaddy.ecommerce.models.OrderStatus.CONFIRMED;
import static com.thetechmaddy.ecommerce.models.OrderStatus.PENDING;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CASH_ON_DELIVERY;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CREDIT_CARD;
import static com.thetechmaddy.ecommerce.utils.TestUtils.getOrderRequest;
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

    @Autowired
    private PaymentsRepository paymentsRepository;

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
                ordersService.createNewOrder(TEST_COGNITO_SUB,
                        getOrderRequest(Integer.MAX_VALUE, TestOrderRequestOptions.builder().build())));
    }

    @Test
    public void testNewOrderInitiateCartNotBelongsToUserException() {
        assertThrows(CartNotBelongsToUserException.class, () ->
                ordersService.createNewOrder("some-other-user",
                        getOrderRequest(cartId, TestOrderRequestOptions.builder().build())));
    }

    @Test
    public void testNewOrderInitiateCartIsEmptyException() {
        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .paymentMode(CREDIT_CARD)
                .createPaymentInfo(true)
                .cartAmount(new BigDecimal("0")).build();

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);

        assertThrows(EmptyCartException.class, () ->
                ordersService.createNewOrder(TEST_COGNITO_SUB, getOrderRequest(cartId, options)));
    }

    @Test
    public void testNewOrderInitiateCashOnDelivery() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CASH_ON_DELIVERY)
                .build();

        OrderRequest orderRequest = getOrderRequest(cartId, options);

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);
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
    public void testInitiateOrderReturnExisting() {
        assertNull(ordersService.getUserOrderInPendingStatus(TEST_COGNITO_SUB));

        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CREDIT_CARD)
                .build();

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        Order actual = ordersService.getUserOrderInPendingStatus(TEST_COGNITO_SUB);
        assertEquals(order.getId(), actual.getId());
    }

    @Test
    public void testCreateOrderDuplicateError() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CREDIT_CARD)
                .build();

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        assertThrows(DuplicatePendingOrderException.class, () -> ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest));
    }

    @Test
    public void testNewOrderInitiateOtherPaymentModes() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CREDIT_CARD)
                .build();

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);
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
        assertEquals(
                getTestProducts().stream().filter(Product::isInStock).toList().size(),
                cartItemsRepository.countByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB)
        );
    }

    @Test
    @Transactional
    public void testDuplicateOrderException() {
        getTestProducts().stream().filter(Product::isInStock)
                .forEach(product -> cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId));

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CREDIT_CARD)
                .build();


        OrderRequest orderRequest = getOrderRequest(cartId, options);
        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        Order order2 = new Order(order);
        ordersRepository.save(order2);

        assertThrows(DuplicatePendingOrderException.class, () -> ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest));
    }

    @Test
    public void testPlaceOrderUnprocessableOrderException() {
        Order order = ordersRepository.save(new Order(OrderStatus.COMPLETED, TEST_COGNITO_SUB));
        assertThrows(UnProcessableEntityException.class, () -> ordersService.placeOrder(order.getId(), new CognitoUser(TEST_COGNITO_SUB)));
        ordersRepository.deleteAll();
    }

    @Test
    public void testPlaceOrderPaymentRequiredException() {
        Order testOrder = new Order(PENDING, TEST_COGNITO_SUB);
        Payment payment = new Payment(PaymentStatus.PENDING, CREDIT_CARD);
        payment.setOrder(testOrder);
        testOrder.setPayment(payment);

        Order order = ordersRepository.save(testOrder);
        assertThrows(PaymentRequiredException.class, () -> ordersService.placeOrder(order.getId(), new CognitoUser(TEST_COGNITO_SUB)));
        ordersRepository.deleteAll();
    }

    @Test
    public void testPlaceOrderPaymentNotInSuccessException() {
        Order testOrder = new Order(PENDING, TEST_COGNITO_SUB);
        Payment payment = new Payment(PaymentStatus.FAILED, CREDIT_CARD);
        payment.setOrder(testOrder);
        testOrder.setPayment(payment);

        Order order = ordersRepository.save(testOrder);
        assertThrows(PaymentNotInSuccessException.class, () -> ordersService.placeOrder(order.getId(), new CognitoUser(TEST_COGNITO_SUB)));
        ordersRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testPlaceOrder() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CREDIT_CARD)
                .build();

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        Order order = ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);

        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentsRepository.save(payment);

        Order placedOrder = ordersService.placeOrder(order.getId(), new CognitoUser(TEST_COGNITO_SUB));
        assertNotNull(placedOrder);
        assertEquals(CONFIRMED, placedOrder.getStatus());

        order.getOrderItems().forEach(oi -> assertEquals(OrderItemStatus.CONFIRMED, oi.getStatus()));

        assertTrue(cartsRepository.isUnlocked(cartId, TEST_COGNITO_SUB));
        assertEquals(0, cartItemsRepository.countByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB));
    }

    @AfterEach
    public void clearOrdersAndCartItems() {
        ordersRepository.deleteAll();
        cartItemsRepository.deleteAll();
        Optional<Cart> cartOptional = cartsRepository.findByUserId(TEST_COGNITO_SUB)
                .map(c -> {
                    c.setCartStatus(UN_LOCKED);
                    return c;
                });

        cartOptional.ifPresent(cart -> cartsRepository.save(cart));
    }
}
