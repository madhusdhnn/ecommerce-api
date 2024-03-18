package com.thetechmaddy.ecommerce.utils;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.exceptions.CartLockedException;
import com.thetechmaddy.ecommerce.exceptions.CartNotBelongsToUserException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartUtils {

    public static void verifyCartOwnerAndLockStatus(String userId, Cart cart) {
        if (cart == null) {
            throw new NullPointerException("cart == null");
        }

        if (!cart.belongsTo(userId)) {
            throw new CartNotBelongsToUserException(
                    String.format("Cart: (cartId - %d) does not belong to the user: (userId - %s)", cart.getId(), userId)
            );
        }

        if (cart.isLocked()) {
            throw new CartLockedException(cart.getId());
        }
    }
}
