package com.bank.transfer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneTransferDto {
    private Long id;
    private Long phoneNumber;
    private BigDecimal amount;
    private String purpose;
    private Long accountDetailsId;
}
