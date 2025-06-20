package com.bank.profile.Exceptions;

/**
 * Исключение, которое возникает во время создания уже существующего профиля
 */
public class EntityDublicateException extends RuntimeException {
    public EntityDublicateException(String message) {
        super(message);
    }
}
