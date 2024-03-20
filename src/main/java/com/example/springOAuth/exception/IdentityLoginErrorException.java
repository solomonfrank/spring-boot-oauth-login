package com.example.springOAuth.exception;

public class IdentityLoginErrorException extends RuntimeException {

    public IdentityLoginErrorException(String message) {
        super(message);
    }

    public IdentityLoginErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
