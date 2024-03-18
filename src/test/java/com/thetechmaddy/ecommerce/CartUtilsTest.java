package com.thetechmaddy.ecommerce;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.exceptions.CartLockedException;
import com.thetechmaddy.ecommerce.exceptions.CartNotBelongsToUserException;
import org.junit.jupiter.api.Test;

import static com.thetechmaddy.ecommerce.models.CartStatus.LOCKED;
import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static com.thetechmaddy.ecommerce.utils.CartUtils.verifyCartOwnerAndLockStatus;
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
}
