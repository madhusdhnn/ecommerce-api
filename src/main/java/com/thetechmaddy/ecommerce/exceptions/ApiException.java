package com.thetechmaddy.ecommerce.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApiException(String message, HttpStatus statusCode) {
        super(message);
        this.status = statusCode;
    }

    public ApiException(String message, HttpStatus statusCode, Throwable cause) {
        super(message, cause);
        this.status = statusCode;

    }
}
