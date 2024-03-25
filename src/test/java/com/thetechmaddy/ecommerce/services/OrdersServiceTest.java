package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.Address;
import com.thetechmaddy.ecommerce.domains.DeliveryDetails;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.OrderItemStatus;
import com.thetechmaddy.ecommerce.models.OrderStatus;
import com.thetechmaddy.ecommerce.models.delivery.Customer;
import com.thetechmaddy.ecommerce.models.delivery.DeliveryInfo;
import com.thetechmaddy.ecommerce.models.filters.OrderFilters;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentStatus;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.models.requests.OrderRequest;
import com.thetechmaddy.ecommerce.models.responses.Paged;
import com.thetechmaddy.ecommerce.repositories.*;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.OrderStatus.*;
import static com.thetechmaddy.ecommerce.models.carts.CartStatus.UN_LOCKED;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.*;
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

    @Autowired
    private DeliveryDetailsRepository deliveryDetailsRepository;

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

        BigDecimal cartTotal = cartItemsRepository.getGrossTotalForSelectedCartItems(cartId, TEST_COGNITO_SUB);

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

        Order order = createTestOrderWithCreditCardPaymentMode();

        Order actual = ordersService.getUserOrderInPendingStatus(TEST_COGNITO_SUB);
        assertEquals(order.getId(), actual.getId());

        ordersRepository.save(new Order(PENDING, TEST_COGNITO_SUB));
        DuplicatePendingOrderException ex = assertThrows(DuplicatePendingOrderException.class,
                () -> ordersService.getUserOrderInPendingStatus(TEST_COGNITO_SUB));

        String message = String.format("Found more than one PENDING order for user: (userId - %s)", TEST_COGNITO_SUB);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testCreateOrderDuplicateError() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getGrossTotalForSelectedCartItems(cartId, TEST_COGNITO_SUB);

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
        Order order = createTestOrderWithCreditCardPaymentMode();
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

        BigDecimal cartTotal = cartItemsRepository.getGrossTotalForSelectedCartItems(cartId, TEST_COGNITO_SUB);

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
        Order order = createTestOrderWithCreditCardPaymentMode();

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

    @Test
    public void testGetOrderThrowNotFound() {
        assertThrows(OrderNotFoundException.class, () -> ordersService.getOrder(Integer.MAX_VALUE, TEST_COGNITO_SUB));
    }

    @Test
    public void testGetOrderDoesNotThrowNotFound() {
        Order order = ordersRepository.save(new Order(PENDING, TEST_COGNITO_SUB));
        assertDoesNotThrow(() -> ordersService.getOrder(order.getId(), TEST_COGNITO_SUB));
    }

    @Test
    public void testGetAllOrdersWithoutFilters() {
        createTestOrderWithCreditCardPaymentMode();

        Paged<Order> result = ordersService.getUserOrders(0, 2, TEST_COGNITO_SUB, null);
        assertNotNull(result);

        List<Order> orders = result.data();
        assertEquals(1, orders.size());

        result = ordersService.getUserOrders(0, 2, TEST_COGNITO_SUB, OrderFilters.emptyFilters());
        assertNotNull(result);

        orders = result.data();
        assertEquals(1, orders.size());
    }

    @Test
    public void testGetAllOrdersWithFilters1() {
        createTestOrderWithCreditCardPaymentMode();

        Paged<Order> result = ordersService.getUserOrders(0, 2, TEST_COGNITO_SUB, OrderFilters.builder().year(2023).build());
        assertNotNull(result);

        List<Order> orders = result.data();
        assertTrue(orders.isEmpty());
        assertEquals(0, result.total());
    }

    @Test
    public void testGetAllOrdersWithFilters2() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        order.setCreatedAt(OffsetDateTime.now().minusDays(34));
        ordersRepository.save(order);

        Paged<Order> result = ordersService.getUserOrders(0, 2, TEST_COGNITO_SUB, OrderFilters.builder().last30Days(true).build());
        assertNotNull(result);

        List<Order> orders = result.data();
        assertTrue(orders.isEmpty());
        assertEquals(0, result.total());
    }

    @Test
    public void testGetAllOrdersWithFilters3() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        order.setCreatedAt(OffsetDateTime.now().minusMonths(4));
        ordersRepository.save(order);

        Paged<Order> result = ordersService.getUserOrders(0, 2, TEST_COGNITO_SUB, OrderFilters.builder().past3Months(true).build());
        assertNotNull(result);

        List<Order> orders = result.data();
        assertTrue(orders.isEmpty());
        assertEquals(0, result.total());
    }

    @Test
    public void testGetAllOrdersWithFilters4() {
        createTestOrderWithCreditCardPaymentMode();

        Paged<Order> result = ordersService.getUserOrders(0, 2, TEST_COGNITO_SUB, OrderFilters.builder().orderStatus(COMPLETED).build());
        assertNotNull(result);

        List<Order> orders = result.data();
        assertTrue(orders.isEmpty());
        assertEquals(0, result.total());
    }

    @Test
    public void testUpdatePaymentInfoFailed1() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        order.setStatus(CONFIRMED);
        ordersRepository.save(order);

        assertThrows(UnProcessableEntityException.class,
                () -> ordersService.updatePaymentInfo(
                        order.getId(), TEST_COGNITO_SUB,
                        new PaymentInfo(order.getGrossTotal(), CREDIT_CARD)
                )
        );
    }

    @Test
    public void testUpdatePaymentInfoFailed2() {
        Order order = createTestOrderWithCreditCardPaymentMode();

        assertThrows(OrderItemsTotalMismatchException.class,
                () -> ordersService.updatePaymentInfo(
                        order.getId(), TEST_COGNITO_SUB,
                        new PaymentInfo(order.getGrossTotal().add(new BigDecimal("100.2")), CREDIT_CARD)
                )
        );
    }

    @Test
    public void testUpdatePaymentInfoFailed3() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.PROCESSING);
        order.setPayment(payment);
        ordersRepository.save(order);

        assertThrows(UnProcessableEntityException.class,
                () -> ordersService.updatePaymentInfo(
                        order.getId(), TEST_COGNITO_SUB,
                        new PaymentInfo(order.getGrossTotal(), CREDIT_CARD)
                )
        );
    }

    @Test
    public void testUpdatePaymentInfoFailed4() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        Payment payment = order.getPayment();
        payment.setStatus(PaymentStatus.SUCCESS);
        order.setPayment(payment);
        ordersRepository.save(order);

        assertThrows(UnProcessableEntityException.class,
                () -> ordersService.updatePaymentInfo(
                        order.getId(), TEST_COGNITO_SUB,
                        new PaymentInfo(order.getGrossTotal(), CREDIT_CARD)
                )
        );
    }

    @Test
    public void testUpdatePaymentInfoSuccess() {
        Order order = createTestOrderWithCreditCardPaymentMode();

        Payment payment = order.getPayment();
        assertNotNull(payment);
        assertEquals(CREDIT_CARD, payment.getPaymentMode());

        ordersService.updatePaymentInfo(
                order.getId(), TEST_COGNITO_SUB,
                new PaymentInfo(order.getGrossTotal(), DEBIT_CARD));

        Order updatedOrder = ordersService.getOrder(order.getId(), TEST_COGNITO_SUB);
        assertNotNull(updatedOrder);

        Payment updatedPayment = updatedOrder.getPayment();
        assertNotNull(updatedPayment);
        assertEquals(DEBIT_CARD, updatedPayment.getPaymentMode());
    }

    @Test
    public void testUpdateDeliveryInfoFailed1() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        order.setStatus(CONFIRMED);
        ordersRepository.save(order);

        assertThrows(UnProcessableEntityException.class,
                () -> ordersService.updateDeliveryInfo(
                        order.getId(), TEST_COGNITO_SUB,
                        null
                )
        );
    }

    @Test
    public void testUpdateDeliveryInfoSuccess() {
        Order order = createTestOrderWithCreditCardPaymentMode();

        Address shippingAddress = Address.builder()
                .addressOne("Wall St")
                .zipCode("567234")
                .build();

        Address billingAddress = Address.builder()
                .addressOne("Peter St")
                .zipCode("567244")
                .build();

        DeliveryInfo updateBillingAddressInput = new DeliveryInfo(
                new Customer("Jane Doe", "jane.doe@example.com"),
                shippingAddress,
                billingAddress
        );

        ordersService.updateDeliveryInfo(
                order.getId(), TEST_COGNITO_SUB,
                updateBillingAddressInput
        );

        order = ordersService.getOrder(order.getId(), TEST_COGNITO_SUB);
        DeliveryDetails deliveryDetails = order.getDeliveryDetails();
        assertNotNull(deliveryDetails);

        assertEquals("Peter St", deliveryDetails.getBillingAddressOne());
    }

    @Test
    public void testDeleteDraftOrderFailed() {
        Order order = createTestOrderWithCreditCardPaymentMode();
        order.setStatus(CONFIRMED);
        ordersRepository.save(order);

        assertThrows(UnProcessableEntityException.class, () -> ordersService.deleteDraftOrder(order.getId(), TEST_COGNITO_SUB));
    }

    @Test
    public void testDeleteDraftOrderSuccess() {
        Order order = createTestOrderWithCreditCardPaymentMode();

        long orderId = order.getId();
        ordersService.deleteDraftOrder(orderId, TEST_COGNITO_SUB);

        assertTrue(ordersRepository.findByIdAndUserId(orderId, TEST_COGNITO_SUB).isEmpty());
        assertTrue(paymentsRepository.findByOrderId(orderId).isEmpty());
        assertTrue(deliveryDetailsRepository.findByOrderId(orderId).isEmpty());

        assertTrue(cartsRepository.isUnlocked(cartId, TEST_COGNITO_SUB));

        // just unlocked, not cleared
        assertEquals(4, cartItemsRepository.countByCartIdAndCartUserId(cartId, TEST_COGNITO_SUB));
    }

    private Order createTestOrderWithCreditCardPaymentMode() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> {
            cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId);
        });

        BigDecimal cartTotal = cartItemsRepository.getGrossTotalForSelectedCartItems(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .cartAmount(cartTotal)
                .paymentMode(CREDIT_CARD)
                .build();

        cartsRepository.lockCart(cartId, TEST_COGNITO_SUB);

        OrderRequest orderRequest = getOrderRequest(cartId, options);
        return ordersService.createNewOrder(TEST_COGNITO_SUB, orderRequest);
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
