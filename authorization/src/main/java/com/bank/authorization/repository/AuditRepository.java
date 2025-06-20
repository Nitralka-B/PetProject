package com.bank.authorization.repository;

import com.bank.authorization.entity.Audit;
import com.bank.authorization.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью Audit.
 * Позволяет выполнять CRUD-операции и находить последние записи по типу сущности и операции.
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    Audit findFirstByEntityTypeAndOperationTypeOrderByCreatedAtDesc(String entityType, OperationType operationType);

}
