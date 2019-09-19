package com.crossworld.web.errors.exceptions;

public class SecurityContextEmptyException extends RuntimeException {

    public SecurityContextEmptyException(String message) {
        super(message);
    }
}
