package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class ProductNotInCartException extends BusinessException {

    public ProductNotInCartException(long productId) {
        super(String.format("Product: (productId - %d) not in cart.", productId), UNPROCESSABLE_ENTITY);
    }

}
