package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class PaymentNotInSuccessException extends BusinessException {

    public PaymentNotInSuccessException(String message) {
        super(message, UNPROCESSABLE_ENTITY);
    }

}
