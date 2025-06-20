package com.bank.profile.Exceptions;

/**
 * Исключение, которое возникает во время выполнения аудита
 */
public class AuditException extends RuntimeException {
    public AuditException(String message) {
        super(message);
    }
}
