package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;

public class UnprocessableOrderException extends BusinessException {

    public UnprocessableOrderException(String message) {
        super(message, FAILED_DEPENDENCY);
    }

}
