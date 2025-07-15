package com.bank.transfer.entities;

import com.bank.transfer.Util.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class CardTransfer {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private Long cardNumber;

    @Column(precision = Constants.AMOUNT_PRECISION, scale = Constants.AMOUNT_SCALE, nullable = false)
    private BigDecimal amount;

    @Column(columnDefinition = "TEXT")
    private String purpose;

    @Column(nullable = false)
    private Long accountDetailsId;

}
