package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException(long orderId) {
        super(String.format("Order: (orderId - %d) does not exist", orderId), FORBIDDEN);
    }
}
