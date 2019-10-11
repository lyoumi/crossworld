package com.cwd.tg.gss.errors.exceptions;

public class TokenValidationException extends RuntimeException {

    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException(String message, RuntimeException e) {
        super(message, e);
    }
}
