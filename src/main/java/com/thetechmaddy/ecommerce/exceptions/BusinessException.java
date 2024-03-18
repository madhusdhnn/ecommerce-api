package com.thetechmaddy.ecommerce.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(String message, HttpStatus statusCode) {
        super(message);
        this.status = statusCode;
    }

    public BusinessException(String message, HttpStatus statusCode, Throwable cause) {
        super(message, cause);
        this.status = statusCode;
    }

    public Map<String, Object> getAdditionalInfo() {
        return new HashMap<>(Collections.emptyMap());
    }
}
