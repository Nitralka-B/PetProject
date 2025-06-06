package com.bank.antifraud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Класс для приемки данных из другого микросервиса (transfer)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferChecked {
    private Long id;
    private Long number;
    private BigDecimal amount;
    private String purpose;
    private Long accountDetailsId;
}
