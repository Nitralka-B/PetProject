package com.bank.transfer.repositories;

import com.bank.transfer.entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    Optional<Audit> findFirstByEntityTypeAndEntityJsonContainsOrderByCreatedAtDesc
            (String entityType, String idFragment);
}
