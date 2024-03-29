package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(long orderId) {
        super(String.format("Order: (orderId - %d) details not found", orderId), BAD_REQUEST);
    }

    public OrderNotFoundException(String message) {
        super(message, BAD_REQUEST);
    }
}
