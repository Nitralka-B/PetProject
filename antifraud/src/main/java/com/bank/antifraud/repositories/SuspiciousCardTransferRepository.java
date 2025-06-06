package com.bank.antifraud.repositories;

import com.bank.antifraud.entities.SuspiciousCardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для подозрительных переводов по карте.
 */
@Repository
public interface SuspiciousCardTransferRepository extends JpaRepository<SuspiciousCardTransfer, Long> {
    boolean existsByCardTransferId(Long cardTransferId);
    Optional<SuspiciousCardTransfer> findByCardTransferId(Long cardTransferId);
}
