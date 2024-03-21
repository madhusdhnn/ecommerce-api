package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;

public class UnProcessableEntityException extends BusinessException {

    public UnProcessableEntityException(String message) {
        super(message, FAILED_DEPENDENCY);
    }

}
