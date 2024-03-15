package com.thetechmaddy.ecommerce.domains;

import com.thetechmaddy.ecommerce.models.CartItemStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

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

    @Test
    public void testCartItemsSubTotalOnlySelectedItems() {
        Cart cart = new Cart("user_id", UN_LOCKED);
        Set<CartItem> cartItems = Set.of(
                new CartItem(new Product("p1", new BigDecimal("459.45")), 2, CartItemStatus.SELECTED, cart),
                new CartItem(new Product("p2", new BigDecimal("4539.65")), 1, CartItemStatus.SELECTED, cart),
                new CartItem(new Product("p3", new BigDecimal("4539.65")), 1, CartItemStatus.UN_SELECTED, cart),
                new CartItem(new Product("p4", new BigDecimal("5539.65")), 2, CartItemStatus.UN_SELECTED, cart)
        );
        cart.setCartItems(cartItems);

        assertEquals(BigDecimal.ZERO, cart.getSubTotal());
        cart.calculateSubTotal();
        assertEquals(new BigDecimal("5458.55"), cart.getSubTotal());
    }
}
