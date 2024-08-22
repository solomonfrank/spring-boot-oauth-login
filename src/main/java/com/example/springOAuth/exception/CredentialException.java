package com.example.springOAuth.exception;

public class CredentialException extends RuntimeException {

    public CredentialException(String message) {
        super(message);
    }

    public CredentialException(String message, Throwable t) {
        super(message, t);
    }

}
