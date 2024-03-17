package com.thetechmaddy.ecommerce.exceptions;

import static org.springframework.http.HttpStatus.CONFLICT;

public class DuplicatePendingOrderException extends BusinessException {

    public DuplicatePendingOrderException(String message) {
        super(message, CONFLICT);
    }

}
