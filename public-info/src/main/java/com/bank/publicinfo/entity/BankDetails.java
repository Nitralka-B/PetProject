package com.bank.publicinfo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность для банковских реквизитов.
 * Содержит официальные банковские идентификаторы и регистрационную информацию.
 */
@Entity
@Table(name = "bank_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetails implements AuditableEntity {
    private static final int CITY_MAX_LENGTH = 180;
    private static final int COMPANY_CODE_MAX_LENGTH = 15;
    private static final int BANK_NAME_MAX_LENGTH = 80;
    private static final int BIK_LENGTH = 9;
    private static final int INN_LEGAL_LENGTH = 10;
    private static final int INN_INDIVIDUAL_LENGTH = 12;
    private static final int KPP_LENGTH = 9;
    private static final int COR_ACCOUNT_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long bik;

    @Column(nullable = false, unique = true)
    private Long inn;

    @Column(nullable = false, unique = true)
    private Long kpp;

    @Column(name = "cor_account", nullable = false, unique = true)
    private Integer corAccount;

    @Column(nullable = false, length = CITY_MAX_LENGTH)
    private String city;

    @Column(name = "joint_stock_company", nullable = false, length = COMPANY_CODE_MAX_LENGTH)
    private String jointStockCompany;

    @Column(nullable = false, length = BANK_NAME_MAX_LENGTH)
    private String name;

    @Override
    public Long getId() {
        return this.id;
    }
}
