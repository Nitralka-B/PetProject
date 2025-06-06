package com.bank.antifraud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность для подозрительных переводов по телефону.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suspicious_phone_transfers", schema = "anti_fraud")
public class SuspiciousPhoneTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_transfer_id", unique = true)
    private Long phoneTransferId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "is_suspicious")
    private Boolean isSuspicious;

    @Column(name = "blocked_reason", columnDefinition = "text")
    private String blockedReason;

    @Column(name = "suspicious_reason", columnDefinition = "text")
    private String suspiciousReason;
}

