package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.BaseIntegrationTest;
import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.orders.Order;
import com.thetechmaddy.ecommerce.domains.payments.Payment;
import com.thetechmaddy.ecommerce.domains.products.Product;
import com.thetechmaddy.ecommerce.exceptions.PaymentNotFoundException;
import com.thetechmaddy.ecommerce.models.payments.CardPaymentInfo;
import com.thetechmaddy.ecommerce.models.requests.CognitoUser;
import com.thetechmaddy.ecommerce.repositories.CartItemsRepository;
import com.thetechmaddy.ecommerce.repositories.CartsRepository;
import com.thetechmaddy.ecommerce.repositories.OrdersRepository;
import com.thetechmaddy.ecommerce.utils.TestUtils.TestOrderRequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.CREDIT_CARD;
import static com.thetechmaddy.ecommerce.models.payments.PaymentMode.UPI;
import static com.thetechmaddy.ecommerce.utils.TestUtils.getOrderRequest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PaymentServiceTest extends BaseIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    private long cartId;

    @BeforeAll
    public void setupCart() {
        ordersRepository.deleteAll();
        ordersRepository.flush();
        cartsRepository.deleteAll();

        Cart cart = cartsRepository.save(new Cart(TEST_COGNITO_SUB, UN_LOCKED));
        cartId = cart.getId();

    }

    @Test
    public void testProcessPaymentError() {
        Assertions.assertThrows(PaymentNotFoundException.class,
                () -> paymentService.processPayment(Integer.MAX_VALUE, null, new CognitoUser(TEST_COGNITO_SUB))
        );
    }

    @Test
    public void testProcessPaymentSuccess() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId));

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .paymentMode(CREDIT_CARD)
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .shippingSameAsBilling(true)
                .cartAmount(cartTotal)
                .build();

        Order newOrder = ordersService.createNewOrder(TEST_COGNITO_SUB, getOrderRequest(cartId, options));
        assertNotNull(newOrder);
        assertTrue(newOrder.isPending());

        Payment payment = paymentService.processPayment(newOrder.getPayment().getId(), new CardPaymentInfo(), new CognitoUser(TEST_COGNITO_SUB));
        assertNotNull(payment);
        assertTrue(payment.isSuccess());

        Payment idempotentPayment = paymentService.processPayment(newOrder.getPayment().getId(), new CardPaymentInfo(), new CognitoUser(TEST_COGNITO_SUB));
        assertEquals(payment, idempotentPayment);
    }

    @Test
    public void testProcessPaymentFailedFromGateway() {
        getTestProducts().stream().filter(Product::isInStock).forEach(product -> cartItemsRepository.saveOnConflictUpdateQuantity(product.getId(), 3, cartId));

        BigDecimal cartTotal = cartItemsRepository.getTotal(cartId, TEST_COGNITO_SUB);

        TestOrderRequestOptions options = TestOrderRequestOptions.builder()
                .paymentMode(UPI)
                .createDeliveryInfo(true)
                .createPaymentInfo(true)
                .shippingSameAsBilling(true)
                .cartAmount(cartTotal)
                .build();

        Order newOrder = ordersService.createNewOrder(TEST_COGNITO_SUB, getOrderRequest(cartId, options));
        assertNotNull(newOrder);
        assertTrue(newOrder.isPending());

        Payment payment = paymentService.processPayment(newOrder.getPayment().getId(), new CardPaymentInfo(), new CognitoUser(TEST_COGNITO_SUB));
        assertNotNull(payment);
        assertFalse(payment.isSuccess());

        Payment idempotentPayment = paymentService.processPayment(newOrder.getPayment().getId(), new CardPaymentInfo(), new CognitoUser(TEST_COGNITO_SUB));
        assertEquals(payment, idempotentPayment);
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
