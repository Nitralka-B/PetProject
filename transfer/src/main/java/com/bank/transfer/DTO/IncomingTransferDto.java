package com.bank.transfer.DTO;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
