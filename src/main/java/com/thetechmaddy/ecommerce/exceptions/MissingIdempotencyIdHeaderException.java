package com.thetechmaddy.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class MissingIdempotencyIdHeaderException extends BusinessException {

    public MissingIdempotencyIdHeaderException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
