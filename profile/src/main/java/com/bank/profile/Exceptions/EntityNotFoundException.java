package com.bank.profile.Exceptions;

/**
 * Исключение, которое возникает при отсутствии нужной сущности
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
