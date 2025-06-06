package com.bank.antifraud.repositories;

import com.bank.antifraud.entities.SuspiciousAccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Репозиторий для подозрительных переводов по аккаунту.
 */
@Repository
public interface SuspiciousAccountTransferRepository extends JpaRepository<SuspiciousAccountTransfer, Long> {
    boolean existsByAccountTransferId(Long accountTransferId);
    Optional<SuspiciousAccountTransfer> findByAccountTransferId(Long accountTransferId);
}
