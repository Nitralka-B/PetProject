package com.bank.account.repository;

import com.bank.account.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для управления сущностями {@link Audit}.
 * <p>
 * Расширяет {@link JpaRepository}, предоставляя базовые CRUD-операции.
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
}
