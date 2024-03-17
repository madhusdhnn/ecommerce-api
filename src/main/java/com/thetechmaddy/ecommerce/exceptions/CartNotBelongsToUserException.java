package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CartNotBelongsToUserException extends BusinessException {

    public CartNotBelongsToUserException(String message) {
        super(message, FORBIDDEN);
    }

}
