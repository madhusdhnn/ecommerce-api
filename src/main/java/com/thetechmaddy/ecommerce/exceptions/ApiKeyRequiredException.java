package com.thetechmaddy.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class ApiKeyRequiredException extends BusinessException {

    public ApiKeyRequiredException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
