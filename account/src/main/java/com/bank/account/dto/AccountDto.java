package com.bank.account.dto;

import com.bank.account.validator.UniqueAccountData;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO для класса Account. Совпадает с самим классом.
 * Передача данных учетной записи через Kafka.
 */

@Getter
@Setter
@UniqueAccountData

public class AccountDto {
    private Long id;

    @NotNull(message = "Passport id is necessary")
    private Long passportId;

    @NotNull(message = "Account number is necessary")
    private Long accountNumber;

    @NotNull(message = "Bank details id is necessary")
    private Long bankDetailsId;

    @Digits(integer = 18, fraction = 2, message = "Maximum 18 digits to the comma and 2 after")
    @NotNull(message = "Money amount is necessary")
    private BigDecimal money;

    @NotNull(message = "Balance type is necessary")
    private boolean negativeBalance;

    @NotNull(message = "Profile id is necessary")
    private Long profileId;


}
