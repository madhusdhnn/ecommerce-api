package com.thetechmaddy.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class CartNotLockedException extends BusinessException {
    public CartNotLockedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
