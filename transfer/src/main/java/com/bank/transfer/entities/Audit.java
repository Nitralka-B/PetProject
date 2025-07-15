package com.bank.transfer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(40)", nullable = false)
    private String entityType;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String operationType;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String createdBy;

    @Column(columnDefinition = "VARCHAR(255)")
    private String modifiedBy;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(columnDefinition = "TEXT")
    private String newEntityJson;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String entityJson;
}
