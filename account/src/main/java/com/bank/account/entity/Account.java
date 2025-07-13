package com.bank.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Модель учетной записи.
 */

@Entity
@Getter
@Setter
@Table(name = "account_details")
public class Account {
    private static final int PRECISION = 20;
    private static final int SCALE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "account_number", unique = true)
    private Long accountNumber;

    @Column(name = "bank_details_id", unique = true)
    private Long bankDetailsId;

    @Column(name = "money", precision = PRECISION, scale = SCALE)
    private BigDecimal money;

    @Column(name = "negative_balance")
    private boolean negativeBalance;

    @Column(name = "profile_id")
    private Long profileId;
}
