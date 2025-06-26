package com.bank.transfer.DTO;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IncomingTransferDto {

    private Long id;
    private String typeOfTransfer;
    private Long number;
    private BigDecimal amount;
    private String purpose;
    private Long accountDetailsId;
}
