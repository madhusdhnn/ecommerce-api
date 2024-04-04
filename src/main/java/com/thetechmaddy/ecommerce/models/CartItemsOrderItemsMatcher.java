package com.thetechmaddy.ecommerce.models;

import com.thetechmaddy.ecommerce.domains.carts.CartItem;
import com.thetechmaddy.ecommerce.domains.orders.OrderItem;
import com.thetechmaddy.ecommerce.domains.products.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class CartItemsOrderItemsMatcher {

    private final List<CartItem> cartItems;
    private final List<OrderItem> orderItems;

    public boolean matches() {
        if (cartItems.size() != orderItems.size()) {
            return false;
        }

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem cartItem = cartItems.get(i);
            Product productInCart = cartItem.getProduct();

            OrderItem orderItem = orderItems.get(i);
            Product productInOrder = orderItem.getProduct();

            if (productInCart.getId() != productInOrder.getId()) {
                return false;
            }

            if (cartItem.getQuantity() != orderItem.getQuantity()) {
                return false;
            }
        }
        return true;
    }
}
