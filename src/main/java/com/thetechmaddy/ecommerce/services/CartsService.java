package com.thetechmaddy.ecommerce.services;

import com.thetechmaddy.ecommerce.domains.carts.Cart;
import com.thetechmaddy.ecommerce.models.requests.CartItemRequest;
import com.thetechmaddy.ecommerce.models.requests.CartItemUpdateRequest;

public interface CartsService {

    Cart getCart(long cartId, String userId);

    void addProductToCart(long cartId, String userId, CartItemRequest cartItemRequest);

    void updateProductInCart(long cartId, long productId, String userId, CartItemUpdateRequest cartItemUpdateRequest);

    boolean removeProductFromCart(long cartId, long productId, String userId);

    boolean clearCart(long cartId, String userId);

    boolean lockCart(long cartId, String userId);

    boolean unlockCart(long cartId, String userId);

    Cart getUserCart(String userId);
}
