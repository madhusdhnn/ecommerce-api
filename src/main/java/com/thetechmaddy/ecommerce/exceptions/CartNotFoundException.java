package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CartNotFoundException extends BusinessException {

    public CartNotFoundException(String message) {
        super(message, BAD_REQUEST);
    }

}
