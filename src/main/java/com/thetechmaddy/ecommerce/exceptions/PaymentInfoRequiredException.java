package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;

public class PaymentInfoRequiredException extends BusinessException {
    public PaymentInfoRequiredException(String message) {
        super(message, FAILED_DEPENDENCY);
    }
}
