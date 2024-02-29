package com.example.springOAuth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUserInfoException extends RuntimeException {

    public DuplicateUserInfoException(String message) {
        super(message);
    }

    public DuplicateUserInfoException(String message, Throwable cause) {
        super(message, cause);
    }

}
