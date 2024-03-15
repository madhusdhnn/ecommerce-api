package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ProductNotFoundException extends ApiException {

    public ProductNotFoundException(String message) {
        super(message, NOT_FOUND);
    }

}
