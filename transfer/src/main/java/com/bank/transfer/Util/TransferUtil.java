package com.bank.transfer.Util;


import com.bank.transfer.DTO.*;
import com.bank.transfer.ENUM.TransferType;

import java.math.BigDecimal;

public class TransferUtil {

    public static TransferType resolveTransferType(String type) {
        return switch (type.toLowerCase()) {
            case "card" -> TransferType.CARD;
            case "account" -> TransferType.ACCOUNT;
            case "phone" -> TransferType.PHONE;
            default -> throw new IllegalArgumentException("Unknown transfer type: " + type);
        };
    }
    public static boolean hasSufficientFunds(AccountDetailsDto account, BigDecimal amount) {
        return account.isNegativeBalance() || account.getMoney().compareTo(amount) >= 0;
    }

    public static CardTransferDto mapToCardDto (IncomingTransferDto dto) {
        return new CardTransferDto(dto.getId(), dto.getNumber(), dto.getAmount(),
                dto.getPurpose(), dto.getAccountDetailsId());
    }

    public static AccountTransferDto mapToAccountDto (IncomingTransferDto dto) {
        return new AccountTransferDto(dto.getId(), dto.getNumber(), dto.getAmount(),
                dto.getPurpose(), dto.getAccountDetailsId());
    }

    public static PhoneTransferDto mapToPhoneDto (IncomingTransferDto dto) {
        return new PhoneTransferDto(dto.getId(), dto.getNumber(), dto.getAmount(),
                dto.getPurpose(), dto.getAccountDetailsId());
    }
}
