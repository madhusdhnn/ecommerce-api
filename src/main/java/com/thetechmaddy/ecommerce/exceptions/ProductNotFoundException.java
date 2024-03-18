package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(String message) {
        super(message, FORBIDDEN);
    }

}
