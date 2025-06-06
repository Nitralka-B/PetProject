package com.bank.antifraud.services;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entities.SuspiciousCardTransfer;
import com.bank.antifraud.kafka.SuspiciousTransferProducer;
import com.bank.antifraud.mapper.CardTransferMapper;
import com.bank.antifraud.repositories.SuspiciousCardTransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для работы с подозрительными переводами по карте.
 */
@Service
@Slf4j

public class SuspiciousCardTransferServiceImpl extends AbstractSuspiciousTransferService
        <SuspiciousCardTransferDto,
                SuspiciousCardTransfer,
                SuspiciousCardTransferRepository,
                CardTransferMapper> {

    public SuspiciousCardTransferServiceImpl(SuspiciousCardTransferRepository repository,
                                             CardTransferMapper mapper,
                                             SuspiciousTransferProducer producer,
                                             ObjectMapper objectMapper) {
        super(repository, mapper, producer, objectMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTransferId(Long transferId) {
        return repository.existsByCardTransferId(transferId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findIdByTransferId(Long cardTransferId) {
        return repository.findByCardTransferId(cardTransferId)
                .map(SuspiciousCardTransfer::getId);
    }
}

