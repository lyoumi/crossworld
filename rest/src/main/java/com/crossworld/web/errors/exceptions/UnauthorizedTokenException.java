package com.crossworld.web.errors.exceptions;

public class UnauthorizedTokenException extends RuntimeException {

    public UnauthorizedTokenException(String message) {
        super(message);
    }
}
