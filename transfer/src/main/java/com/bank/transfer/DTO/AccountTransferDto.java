package com.bank.transfer.DTO;

import com.bank.transfer.AOP.Identifiable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferDto implements Identifiable {

    @NotNull
    private Long id;
    private Long accountNumber;
    private BigDecimal amount;
    private String purpose;
    private Long accountDetailsId;

}
