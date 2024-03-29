package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class CartNotLockedException extends BusinessException {
    public CartNotLockedException(String message) {
        super(message, UNPROCESSABLE_ENTITY);
    }
}
