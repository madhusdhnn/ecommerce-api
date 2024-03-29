package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(String message) {
        super(message, BAD_REQUEST);
    }

}
