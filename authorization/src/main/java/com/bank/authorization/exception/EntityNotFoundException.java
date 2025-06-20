package com.bank.authorization.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);

    }

    public EntityNotFoundException(String message, Long id, Throwable cause) {

        super(message + ". ID: " + id, cause);
    }
}
