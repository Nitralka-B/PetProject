package com.bank.antifraud.repositories;

import com.bank.antifraud.entities.SuspiciousPhoneTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Репозиторий для подозрительных переводов по телефону.
 */
@Repository
public interface SuspiciousPhoneTransferRepository extends JpaRepository<SuspiciousPhoneTransfer, Long> {
    boolean existsByPhoneTransferId(Long phoneTransferId);
    Optional<SuspiciousPhoneTransfer> findByPhoneTransferId(Long phoneTransferId);
}
