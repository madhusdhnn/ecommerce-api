package com.thetechmaddy.ecommerce.exceptions;

public class AuthenticationMissingException extends RuntimeException {

    public AuthenticationMissingException(String message) {
        super(message);
    }

}
