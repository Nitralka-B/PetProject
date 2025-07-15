package com.bank.transfer.repositories;

import com.bank.transfer.entities.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Long> {
    Optional<AccountTransfer> findByAccountNumber(Long accountNumber);
    boolean existsByAccountNumber(Long accountNumber);
}
