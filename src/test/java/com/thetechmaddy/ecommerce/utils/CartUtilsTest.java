package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import com.thetechmaddy.ecommerce.models.payments.PaymentMode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static com.thetechmaddy.ecommerce.models.carts.CartStatus.LOCKED;
import static com.thetechmaddy.ecommerce.models.carts.CartStatus.UN_LOCKED;
import static com.thetechmaddy.ecommerce.utils.CartUtils.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CartUtilsTest {

    @Test
    public void testCartVerificationNPE() {
        assertThrows(NullPointerException.class, () -> verifyCartOwnerAndLockStatus("user", null));
    }

    @Test
    public void testCartVerificationNoAccessToCart() {
        assertThrows(CartNotBelongsToUserException.class,
                () -> verifyCartOwnerAndLockStatus("user", new Cart("other-user", UN_LOCKED)));
    }

    @Test
    public void testCartVerificationCartLocked() {
        assertThrows(CartLockedException.class,
                () -> verifyCartOwnerAndLockStatus("user", new Cart("user", LOCKED)));
    }

    @Test
    public void testEnsureCartNotEmpty() {
        assertThrows(EmptyCartException.class, () -> ensureCartNotEmpty(Integer.MAX_VALUE, Collections.emptyList()));
        assertDoesNotThrow(() -> ensureCartNotEmpty(Integer.MAX_VALUE, null));
    }

    @Test
    public void testEnsureCartTotalAndPaymentMatches() {
        assertThrows(NullPointerException.class, () -> ensureCartTotalAndPaymentMatches(null, new Cart()));
        assertThrows(NullPointerException.class, () -> ensureCartTotalAndPaymentMatches(new PaymentInfo(), null));

        PaymentInfo paymentInfo = new PaymentInfo(new BigDecimal("100"), PaymentMode.CREDIT_CARD);
        Cart cart = new Cart(new BigDecimal("105"));

        assertThrows(CartItemsTotalMismatchException.class, () -> ensureCartTotalAndPaymentMatches(paymentInfo, cart));
        assertDoesNotThrow(() -> ensureCartTotalAndPaymentMatches(paymentInfo, new Cart(new BigDecimal("100"))));
    }

    @Test
    public void testCartLocked() {
        assertDoesNotThrow(() -> ensureCartLocked(new Cart("user", LOCKED)));
        assertThrows(CartNotLockedException.class, () -> ensureCartLocked(new Cart("user", UN_LOCKED)));

    }
}
