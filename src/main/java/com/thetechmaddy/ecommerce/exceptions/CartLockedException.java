package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class CartLockedException extends BusinessException {

    public CartLockedException(long cartId) {
        super(String.format("Cart: (cartId - %d) is locked.", cartId), UNPROCESSABLE_ENTITY);
    }

}
