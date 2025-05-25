package com.bank.publicinfo.exception;

/**
 * Исключение, выбрасываемое при ошибках валидации.
 */
public class ValidationException extends RuntimeException {
    /**
     * Создает исключение с сообщением об ошибке.
     * @param message сообщение об ошибке
     */
    public ValidationException(String message) {
        super(message);
    }
}