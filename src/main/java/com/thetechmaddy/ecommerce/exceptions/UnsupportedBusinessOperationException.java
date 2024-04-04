package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public class UnsupportedBusinessOperationException extends BusinessException {

    public UnsupportedBusinessOperationException(String message) {
        super(message, NOT_IMPLEMENTED);
    }

}
