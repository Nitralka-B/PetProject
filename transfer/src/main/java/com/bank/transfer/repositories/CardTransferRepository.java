package com.bank.transfer.repositories;

import com.bank.transfer.entities.CardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardTransferRepository extends JpaRepository<CardTransfer, Long> {
    Optional<CardTransfer> findByCardNumber(Long cardNumber);
    boolean existsByCardNumber(Long cardNumber);
}
