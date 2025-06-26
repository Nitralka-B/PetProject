package com.bank.transfer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDetailsDto {
    private Long id;
    private Long passportId;
    private Long accountNumber;
    private Long bankDetailsId;
    private BigDecimal money;
    private boolean negativeBalance;
    private Long profileId;
}
