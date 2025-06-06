package com.bank.antifraud.services;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entities.SuspiciousAccountTransfer;
import com.bank.antifraud.kafka.SuspiciousTransferProducer;
import com.bank.antifraud.mapper.AccountTransferMapper;
import com.bank.antifraud.repositories.SuspiciousAccountTransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для работы с подозрительными переводами по аккаунту.
 */
@Service
@Slf4j
public class SuspiciousAccountTransferServiceImpl extends AbstractSuspiciousTransferService
        <SuspiciousAccountTransferDto,
                SuspiciousAccountTransfer,
                SuspiciousAccountTransferRepository,
                AccountTransferMapper> {

    public SuspiciousAccountTransferServiceImpl(SuspiciousAccountTransferRepository repository,
                                                AccountTransferMapper mapper,
                                                SuspiciousTransferProducer producer,
                                                ObjectMapper objectMapper) {
        super(repository, mapper, producer, objectMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTransferId(Long accountTransferId) {
        return repository.existsByAccountTransferId(accountTransferId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findIdByTransferId(Long accountTransferId) {
        return repository.findByAccountTransferId(accountTransferId)
                .map(SuspiciousAccountTransfer::getId);
    }
}
