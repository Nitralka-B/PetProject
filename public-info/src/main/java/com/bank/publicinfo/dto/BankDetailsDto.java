package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import lombok.NoArgsConstructor;

/**
 * DTO для банковских реквизитов.
 * Содержит официальные идентификаторы банка и регистрационную информацию.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDto implements AuditableEntity {
    /** Уникальный идентификатор */
    private Long id;

    /** БИК банка (9 цифр) */
    @Digits(integer = 9, fraction = 0)
    private Long bik;

    /** ИНН банка (10 или 12 цифр) */
    @Digits(integer = 12, fraction = 0)
    private Long inn;

    /** КПП банка */
    private Long kpp;

    /** Корреспондентский счет */
    private Integer corAccount;

    /** Город регистрации банка */
    private String city;

    /** Акционерное общество */
    private String jointStockCompany;

    /** Наименование банка */
    private String name;

    @Override
    public Long getId() {
        return this.id;
    }
}