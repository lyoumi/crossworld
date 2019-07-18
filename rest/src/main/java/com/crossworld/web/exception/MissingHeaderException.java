package com.crossworld.web.exception;

public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException(String message) {
        super(message);
    }
}
