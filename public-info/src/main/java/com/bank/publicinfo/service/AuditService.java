package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.exception.EntityNotFoundException;

import java.util.List;

/**
 * Интерфейс сервиса для работы с записями аудита.
 */
public interface AuditService {
    /**
     * Получает запись аудита по ID.
     *
     * @param id идентификатор записи аудита
     * @return DTO записи аудита
     * @throws EntityNotFoundException если запись не найдена
     */
    AuditDto getAuditRecordById(Long id);

    /**
     * Логирует изменение сущности.
     *
     * @param auditDto DTO с информацией об изменении
     */
    void log(AuditDto auditDto);

    /**
     * Получает запись аудита по ID (то же что getAuditRecordById).
     *
     * @param id идентификатор записи аудита
     * @return DTO записи аудита
     * @throws EntityNotFoundException если запись не найдена
     */
    AuditDto getById(Long id);

    /**
     * Получает все записи аудита.
     *
     * @return список DTO записей аудита
     */
    List<AuditDto> getAll();

    /**
     * Получает последнее известное состояние сущности из БД.
     *
     * @param id идентификатор сущности
     * @param entityClass класс сущности
     * @return сущность или null, если не найдена
     * @param <T> тип сущности, расширяющий AuditableEntity
     */
    <T extends AuditableEntity> T getLastEntityState(Long id, Class<T> entityClass);
}