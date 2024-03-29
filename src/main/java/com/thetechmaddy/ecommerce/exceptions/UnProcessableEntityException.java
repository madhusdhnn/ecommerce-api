package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class UnProcessableEntityException extends BusinessException {

    public UnProcessableEntityException(String message) {
        super(message, UNPROCESSABLE_ENTITY);
    }

}
