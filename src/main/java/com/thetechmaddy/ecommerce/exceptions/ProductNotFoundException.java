package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.NO_CONTENT;

public class  ProductNotFoundException extends ApiException {

    public ProductNotFoundException(String message) {
        super(message, NO_CONTENT);
    }

}
