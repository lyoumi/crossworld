package com.crossworld.auth.errors.exceptions;

public class TokenExpirationException extends RuntimeException {

    public TokenExpirationException(String message) {
        super(message);
    }
}
