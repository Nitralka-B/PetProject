package com.bank.antifraud.services;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.SuspiciousPhoneTransfer;
import com.bank.antifraud.kafka.SuspiciousTransferProducer;
import com.bank.antifraud.mapper.PhoneTransferMapper;
import com.bank.antifraud.repositories.SuspiciousPhoneTransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для работы с подозрительными переводами по телефону.
 */
@Service
@Slf4j
public class SuspiciousPhoneTransferServiceImpl extends AbstractSuspiciousTransferService
        <SuspiciousPhoneTransferDto,
                SuspiciousPhoneTransfer,
                SuspiciousPhoneTransferRepository,
                PhoneTransferMapper> {

    public SuspiciousPhoneTransferServiceImpl(SuspiciousPhoneTransferRepository repository,
                                              PhoneTransferMapper mapper,
                                              SuspiciousTransferProducer producer,
                                              ObjectMapper objectMapper) {
        super(repository, mapper, producer, objectMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTransferId(Long transferId) {
        return repository.existsByPhoneTransferId(transferId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findIdByTransferId(Long phoneTransferId) {
        return repository.findByPhoneTransferId(phoneTransferId)
                .map(SuspiciousPhoneTransfer::getId);
    }
}
