package com.bank.publicinfo.entity;

/**
 * Интерфейс для сущностей, поддерживающих аудит.
 * Определяет метод получения идентификатора сущности.
 */
public interface AuditableEntity {
    /**
     * Возвращает идентификатор сущности.
     * @return идентификатор сущности
     */
    Long getId();
}