package com.bank.antifraud.repositories;

import com.bank.antifraud.entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для аудита.
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    Audit findFirstByEntityTypeAndIdOrderByCreatedAtDesc(String entityType, Long entityId);
    /** Найти самую первую запись (CREATE) по entityType,
    где в entity_json содержится часть с идентификатором перевода
     */
    Audit findFirstByEntityTypeAndEntityJsonContainingIgnoreCaseOrderByCreatedAtAsc(String entityType,
                                                                                    String entityJsonPart);
}
