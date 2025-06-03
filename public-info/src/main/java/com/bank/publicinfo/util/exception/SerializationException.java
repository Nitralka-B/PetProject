package com.bank.publicinfo.util.exception;

/**
 * Исключение, возникающее при ошибках сериализации/десериализации.
 */
public class SerializationException extends RuntimeException {
    private final Class<?> targetType;

    /**
     * Создает исключение с сообщением и типом целевого объекта.
     * @param message сообщение об ошибке
     * @param targetType тип целевого объекта
     */
    public SerializationException(String message, Class<?> targetType) {
        super(message + " [Type: " + targetType.getSimpleName() + "]");
        this.targetType = targetType;
    }

    /**
     * Создает исключение с сообщением, причиной и типом целевого объекта.
     * @param message сообщение об ошибке
     * @param cause причина ошибки
     * @param targetType тип целевого объекта
     */
    public SerializationException(String message, Throwable cause, Class<?> targetType) {
        super(message + " [Type: " + targetType.getSimpleName() + "]", cause);
        this.targetType = targetType;
    }

    /**
     * Возвращает тип целевого объекта.
     * @return тип целевого объекта
     */
    public Class<?> getTargetType() {
        return targetType;
    }
}