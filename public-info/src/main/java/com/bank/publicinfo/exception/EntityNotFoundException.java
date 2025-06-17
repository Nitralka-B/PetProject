package com.bank.publicinfo.exception;

/**
 * Исключение, выбрасываемое при отсутствии сущности.
 */
public class EntityNotFoundException extends RuntimeException {
    private final Long entityId;

    /**
     * Создает исключение с сообщением и идентификатором сущности.
     * @param message сообщение об ошибке
     * @param entityId идентификатор сущности
     */
    public EntityNotFoundException(String message, Long entityId) {
        super(message + " ID: " + entityId);
        this.entityId = entityId;
    }
    /**
     * Возвращает идентификатор сущности, которая не была найдена.
     * @return идентификатор сущности
     */
    public Long getEntityId() {
        return entityId;
    }
}