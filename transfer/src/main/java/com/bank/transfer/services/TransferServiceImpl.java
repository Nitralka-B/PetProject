package com.bank.transfer.services;


import com.bank.transfer.DTO.AccountDetailsDto;
import com.bank.transfer.DTO.IncomingTransferDto;
import com.bank.transfer.ENUM.TransferType;


import com.bank.transfer.repositories.AccountTransferRepository;
import com.bank.transfer.repositories.CardTransferRepository;
import com.bank.transfer.repositories.PhoneTransferRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import static com.bank.transfer.Util.TransferUtil.hasSufficientFunds;
import static com.bank.transfer.Util.TransferUtil.resolveTransferType;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardTransferRepository cardTransferRepository;
    private final PhoneTransferRepository phoneTransferRepository;
    private final AccountTransferRepository accountTransferRepository;
    private final AccountDetailsService accountDetailsService;
    private final TransferOperationServiceImpl transferOperationService;


    @Override
    public void processTransfer(IncomingTransferDto dto) {
        final AccountDetailsDto account = accountDetailsService.getAccountCache().get(dto.getAccountDetailsId());

        if (account == null) {
            log.warn("Account with ID {} not found", dto.getAccountDetailsId());
            return;
        }

        if (!hasSufficientFunds(account, dto.getAmount())) {
            log.warn("Insufficient funds on account ID {}", account.getId());
            return;
        }

        final TransferType type = resolveTransferType(dto.getTypeOfTransfer());
        final Long number = dto.getNumber(); // ← добавь эту строку



        final boolean exists = transferExists(type, number);
        if (exists) {
            transferOperationService.updateTransfer(dto.getId(), dto, type);
        } else {
            transferOperationService.saveTransfer(dto);
        }

    }
    private boolean transferExists (TransferType type, Long number) {
        return switch (type) {
            case CARD -> cardTransferRepository.existsByCardNumber(number);
            case ACCOUNT -> accountTransferRepository.existsByAccountNumber(number);
            case PHONE -> phoneTransferRepository.existsByPhoneNumber(number);
        };
    }



}
