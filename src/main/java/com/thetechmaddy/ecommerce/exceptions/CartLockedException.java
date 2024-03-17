package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.CONFLICT;

public class CartLockedException extends BusinessException {

    public CartLockedException(long cartId) {
        super(String.format("Cart: (cartId - %d) is locked.", cartId), CONFLICT);
    }

}
