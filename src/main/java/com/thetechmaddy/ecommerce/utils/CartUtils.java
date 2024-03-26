package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.exceptions.*;
import com.thetechmaddy.ecommerce.models.payments.PaymentInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartUtils {

    public static void ensureCartTotalAndPaymentMatches(PaymentInfo paymentInfo, Cart cart) {
        ensureCartNotNull(cart);

        Objects.requireNonNull(paymentInfo, "paymentInfo == null");

        if (paymentInfo.getAmount().compareTo(cart.getGrossTotal()) != 0) {
            throw new CartItemsTotalMismatchException(
                    String.format("Cart items total and payment request amount do not match. Cart Total: %s. Payment requested: %s",
                            cart.getGrossTotal(), paymentInfo.getAmount())
            );
        }
    }

    public static void ensureCartNotEmpty(long cartId, List<CartItem> cartItems) {
        if (cartItems != null && cartItems.isEmpty()) {
            throw new EmptyCartException(String.format("cart is empty: (cartId - %d)", cartId));
        }
    }

    public static void verifyCartOwnerAndLockStatus(String userId, Cart cart) {
        ensureCartNotNull(cart);

        if (!cart.belongsTo(userId)) {
            throw new CartNotBelongsToUserException(
                    String.format("Cart: (cartId - %d) does not belong to the user: (userId - %s)", cart.getId(), userId)
            );
        }

        if (cart.isLocked()) {
            throw new CartLockedException(cart.getId());
        }
    }

    public static void ensureCartLocked(Cart cart) {
        Objects.requireNonNull(cart, "cart == null");
        if (cart.isUnlocked()) {
            throw new CartNotLockedException(String.format("Cart: (cartId - %d) not locked", cart.getId()));
        }
    }

    private static void ensureCartNotNull(Cart cart) {
        Objects.requireNonNull(cart, "cart == null");
    }

    public static void verifyCartOwner(Cart cart, String userId) {
        Objects.requireNonNull(cart);

        if (!cart.belongsTo(userId)) {
            throw new CartNotBelongsToUserException(
                    String.format("Cart:(cartId - %d) does not belong to the user - %s", cart.getId(), userId));
        }
    }
}
