package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException(long idempotencyId) {
        super(String.format("Payment entry not found for id - %d", idempotencyId), BAD_REQUEST);
    }

    public PaymentNotFoundException(String message) {
        super(message, BAD_REQUEST);
    }

}
