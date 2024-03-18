package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.CONFLICT;

public class CartItemsTotalMismatchException extends BusinessException {

    public CartItemsTotalMismatchException(String message) {
        super(message, CONFLICT);
    }

}
