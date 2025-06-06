package com.bank.antifraud.entities;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Сущность для подозрительных переводов по счетам.
 */
@Entity
@Table(name = "suspicious_account_transfers", schema = "anti_fraud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuspiciousAccountTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_transfer_id", unique = true)
    private Long accountTransferId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_suspicious")
    private Boolean isSuspicious;

    @Column(name = "blocked_reason", columnDefinition = "text")
    private String blockedReason;

    @Column(name = "suspicious_reason", columnDefinition = "text")
    private String suspiciousReason;
}
