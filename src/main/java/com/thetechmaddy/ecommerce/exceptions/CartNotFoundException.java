package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CartNotFoundException extends BusinessException {

    public CartNotFoundException(String message) {
        super(message, NOT_FOUND);
    }

}
