package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CartNotFoundException extends BusinessException {

    public CartNotFoundException(String message) {
        super(message, FORBIDDEN);
    }

}
