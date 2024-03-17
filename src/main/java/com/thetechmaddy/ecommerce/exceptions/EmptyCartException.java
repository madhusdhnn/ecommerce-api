package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.CONFLICT;

public class EmptyCartException extends BusinessException {

    public EmptyCartException(String message) {
        super(message, CONFLICT);
    }

}
