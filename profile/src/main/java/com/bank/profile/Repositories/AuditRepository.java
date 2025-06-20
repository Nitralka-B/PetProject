package com.bank.profile.Repositories;

import com.bank.profile.Entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    @Query("SELECT a FROM Audit a WHERE " +
            "a.entityType = :entityType AND " +
            "a.operationType = :operationType AND " +
            "a.entityJson LIKE CONCAT('%\"id\":', :entityId, '%')")
    Optional<Audit> findByEntityTypeAndOperationTypeAndJsonId(
            @Param("entityType") String entityType,
            @Param("operationType") String operationType,
            @Param("entityId") Long entityId
    );
}
