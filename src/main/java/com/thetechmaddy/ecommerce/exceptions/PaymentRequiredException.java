package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;

public class PaymentRequiredException extends BusinessException {

    public PaymentRequiredException(String message) {
        super(message, FAILED_DEPENDENCY);
    }

}
