package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.CONFLICT;

public class PaymentNotInSuccessException extends BusinessException {

    public PaymentNotInSuccessException(String message) {
        super(message, CONFLICT);
    }

}
