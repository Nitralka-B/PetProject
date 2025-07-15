package com.bank.transfer.repositories;


import com.bank.transfer.entities.PhoneTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PhoneTransferRepository extends JpaRepository<PhoneTransfer, Long> {
    Optional<PhoneTransfer> findByPhoneNumber(Long phoneNumber);
    boolean existsByPhoneNumber(Long phoneNumber);
}
