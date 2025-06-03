package com.bank.publicinfo.repository;

import com.bank.publicinfo.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для сущности {@link Audit}.
 * Предоставляет CRUD-операции и функциональность JPA для аудиторских записей.
 */
public interface AuditRepository extends JpaRepository<Audit, Long> {
}