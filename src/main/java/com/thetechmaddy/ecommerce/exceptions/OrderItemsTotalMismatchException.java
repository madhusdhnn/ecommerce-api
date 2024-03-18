package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.CONFLICT;

public class OrderItemsTotalMismatchException extends BusinessException {

    public OrderItemsTotalMismatchException(String message) {
        super(message, CONFLICT);
    }
}
