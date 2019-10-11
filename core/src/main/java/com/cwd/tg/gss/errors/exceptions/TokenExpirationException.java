package com.cwd.tg.gss.errors.exceptions;

public class TokenExpirationException extends RuntimeException {

    public TokenExpirationException(String message, Throwable e) {
        super(message, e);
    }

    public TokenExpirationException(String message) {
        super(message);
    }
}
