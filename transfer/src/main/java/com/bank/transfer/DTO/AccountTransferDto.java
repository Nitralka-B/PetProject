package com.bank.transfer.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferDto {

    @NotNull
    private Long id;
    private Long accountNumber;
    private BigDecimal amount;
    private String purpose;
    private Long accountDetailsId;

}
