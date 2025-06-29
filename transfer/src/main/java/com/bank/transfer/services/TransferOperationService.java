package com.bank.transfer.services;


import com.bank.transfer.DTO.AccountTransferDto;
import com.bank.transfer.DTO.CardTransferDto;
import com.bank.transfer.DTO.IncomingTransferDto;
import com.bank.transfer.DTO.PhoneTransferDto;
import com.bank.transfer.ENUM.TransferType;
import com.bank.transfer.entities.AccountTransfer;
import com.bank.transfer.entities.CardTransfer;
import com.bank.transfer.entities.PhoneTransfer;
import com.bank.transfer.kafka.TransferProducer;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.mapper.CardTransferMapper;
import com.bank.transfer.mapper.PhoneTransferMapper;
import com.bank.transfer.repositories.AccountTransferRepository;
import com.bank.transfer.repositories.CardTransferRepository;
import com.bank.transfer.repositories.PhoneTransferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.bank.transfer.Util.TransferUtil.mapToAccountDto;
import static com.bank.transfer.Util.TransferUtil.mapToCardDto;
import static com.bank.transfer.Util.TransferUtil.mapToPhoneDto;
import static com.bank.transfer.Util.TransferUtil.resolveTransferType;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransferOperationService {
    private final AccountTransferRepository accountTransferRepository;
    private final CardTransferRepository cardTransferRepository;
    private final PhoneTransferRepository phoneTransferRepository;
    private final TransferProducer transferProducer;

    private final AccountTransferMapper accountTransferMapper;
    private final CardTransferMapper cardTransferMapper;
    private final PhoneTransferMapper phoneTransferMapper;


    public Object saveTransfer(IncomingTransferDto dto) {
        log.info("saveTransfer called with dto: {}", dto);


        final TransferType type = resolveTransferType(dto.getTypeOfTransfer().toUpperCase());
        switch (type) {

            case CARD -> {
                final CardTransferDto cardDto = mapToCardDto(dto);
                final CardTransfer entity = cardTransferMapper.toEntity(cardDto);
                cardTransferRepository.save(entity);
                transferProducer.sendTransferToKafka(cardTransferMapper.toDTO(entity));
                return cardTransferMapper.toDTO(entity);
            }

            case ACCOUNT -> {
                final AccountTransferDto accDto = mapToAccountDto(dto);
                final AccountTransfer entity = accountTransferMapper.toEntity(accDto);
                accountTransferRepository.save(entity);
                transferProducer.sendTransferToKafka(accountTransferMapper.toDTO(entity));
                return accountTransferMapper.toDTO(entity);
            }

            case PHONE -> {
                final PhoneTransferDto phoneDto = mapToPhoneDto(dto);
                final PhoneTransfer entity = phoneTransferMapper.toEntity(phoneDto);
                phoneTransferRepository.save(entity);
                transferProducer.sendTransferToKafka(phoneTransferMapper.toDTO(entity));
                return phoneTransferMapper.toDTO(entity);
            }

            default -> throw new IllegalArgumentException("Unsupported transfer type");
        }
    }

    public Object updateTransfer (Long id, IncomingTransferDto dto, TransferType type) {

        switch (type) {
            case ACCOUNT:
                final AccountTransferDto accountDto = mapToAccountDto(dto);
                final AccountTransfer existingAccountTransfer = accountTransferRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("AccountTransfer not found"));
                accountTransferMapper.updateEntity(accountDto, existingAccountTransfer);
                accountTransferRepository.save(existingAccountTransfer);
                transferProducer.sendTransferToKafka(accountTransferMapper.toDTO(existingAccountTransfer));
                return accountTransferMapper.toDTO(existingAccountTransfer);

            case CARD:
                final CardTransferDto cardDto = mapToCardDto(dto);

                final CardTransfer existing = cardTransferRepository.findByCardNumber(dto.getNumber())
                        .orElseThrow(() -> new EntityNotFoundException("CardTransfer not found for cardNumber: " +
                                dto.getNumber()));

                cardTransferMapper.updateEntity(cardDto, existing);

                cardTransferRepository.save(existing);
                transferProducer.sendTransferToKafka(cardTransferMapper.toDTO(existing));
                return cardTransferMapper.toDTO(existing);

            case PHONE:
                final PhoneTransferDto phoneDto = mapToPhoneDto(dto);
                final PhoneTransfer existingPhoneTransfer = phoneTransferRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("PhoneTransfer not found"));

                phoneTransferMapper.updateEntity(phoneDto, existingPhoneTransfer);
                phoneTransferRepository.save(existingPhoneTransfer);
                transferProducer.sendTransferToKafka(phoneTransferMapper.toDTO(existingPhoneTransfer));
                return phoneTransferMapper.toDTO(existingPhoneTransfer);

            default:
                throw new IllegalArgumentException("Unknown transfer type");

        }
    }

}
