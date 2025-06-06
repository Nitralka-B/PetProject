package com.bank.antifraud.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface SuspiciousTransferService<T> {
    T createTransfer(T dto);

    T updateTransfer(Optional<Long> id, T dto);

    void deleteTransfer(Long id);

    List<? extends T> getAllTransfers();

    T getTransferById(Long id);

    boolean existsByTransferId(Long transferId);

    Optional<Long> findIdByTransferId(Long transferId);
}
