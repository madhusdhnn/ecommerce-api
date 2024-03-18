package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;

public class PaymentModeRequiredException extends BusinessException {

    public PaymentModeRequiredException(String message) {
        super(message, FAILED_DEPENDENCY);
    }

}
