package com.bank.account.service;

import com.bank.account.util.ActionType;
import com.bank.account.dto.AuditDto;
import org.aspectj.lang.JoinPoint;
import java.util.List;

/**
 * Интерфейс, определяющий контракт для сервисного слоя аудита действий в системе.
 * <p>
 * Предоставляет методы для логирования операций, а также для CRUD-управления записями аудита.
 */
public interface AuditService {
    /**
     * Выполняет аудит действия, используя информацию из точки JoinPoint.
     *
     * @param joinPoint контекст вызова метода, содержащий информацию о целевом объекте, аргументах и методе
     * @param action    тип выполняемого действия (например, "ADD", "UPDATE")
     */
    void audit(JoinPoint joinPoint, ActionType action);
    /**
     * Возвращает список всех записей аудита.
     *
     * @return список объектов AuditDto
     */
    List<AuditDto> listAudits();
    /**
     * Удаляет запись аудита по идентификатору.
     *
     * @param id идентификатор записи аудита
     */
    void deleteAudit(Long id);
    /**
     * Добавляет новую запись аудита.
     *
     * @param auditDto объект аудита, содержащий информацию об операции
     */
    void addAudit(AuditDto auditDto);
    /**
     * Обновляет существующую запись аудита.
     *
     * @param auditDto объект аудита с обновленными данными
     */
    void updateAudit(AuditDto auditDto);
}
