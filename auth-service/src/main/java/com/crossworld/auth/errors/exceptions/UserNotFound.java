package com.crossworld.auth.errors.exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }
}
