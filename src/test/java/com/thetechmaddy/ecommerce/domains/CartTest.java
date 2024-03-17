package com.thetechmaddy.ecommerce.domains;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import org.junit.jupiter.api.Test;

import static com.thetechmaddy.ecommerce.models.CartStatus.LOCKED;
import static com.thetechmaddy.ecommerce.models.CartStatus.UN_LOCKED;
import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    public void testCartBelongsToUser() {
        Cart cart = new Cart("user_id", UN_LOCKED);
        assertTrue(cart.belongsTo("user_id"));
        assertFalse(cart.belongsTo("other_user_id"));
    }

    @Test
    public void testCartStatus() {
        Cart cart = new Cart("user_id", UN_LOCKED);
        assertEquals(UN_LOCKED, cart.getCartStatus());

        cart.lock();
        assertEquals(LOCKED, cart.getCartStatus());

        cart.unlock();
        assertEquals(UN_LOCKED, cart.getCartStatus());
    }
}
