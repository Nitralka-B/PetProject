package com.bank.publicinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для ответов с информацией о банке.
 * Содержит детали ответа на запросы банковской информации.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankResponseDto {
    /** Уникальный идентификатор запроса */
    private UUID requestId;

    /** Информация о банковских реквизитах */
    private BankDetailsDto bankDetails;

    /** Временная метка генерации ответа */
    private LocalDateTime responseTimestamp;

    /** Статус ответа (SUCCESS, ERROR и т.д.) */
    private String status;
}