package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException(long idempotencyId) {
        super(String.format("Payment entry not found for id - %d", idempotencyId), FORBIDDEN);
    }

    public PaymentNotFoundException(String message) {
        super(message, FORBIDDEN);
    }

}
