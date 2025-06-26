package com.bank.transfer.repositories;


import com.bank.transfer.entities.PhoneTransfer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhoneTransferRepository extends JpaRepository<PhoneTransfer, Long> {
    //Optional<PhoneTransfer> findByCardNumber(Long cardNumber);
    boolean existsByPhoneNumber(Long cardNumber);
}
