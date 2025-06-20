package com.bank.profile.Exceptions;

/**
 * Исключение, которое возникает, когда пользователь не аутентифицирован
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
