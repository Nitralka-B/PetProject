package com.bank.transfer.repositories;

import com.bank.transfer.entities.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Long> {
    //Optional<AccountTransfer> findByCardNumber(Long cardNumber);
    boolean existsByAccountNumber(Long cardNumber);
}
