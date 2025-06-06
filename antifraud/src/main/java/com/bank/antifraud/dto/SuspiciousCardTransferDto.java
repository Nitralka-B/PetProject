package com.bank.antifraud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Передача данных о подозрительных переводах по счетам.
 */
@Getter
@Setter
@Schema(description = "Метаданные подозрительного перевода по счету")
public class SuspiciousCardTransferDto {
    @NotNull(message = "ID записи не может быть пустым")
    @Schema(description = "ID", example = "123")
    private Long id;

    @NotNull(message = "ID перевода не может быть пустым")
    @Schema(description = "ID перевода по счету", example = "123")
    private Long cardTransferId;

    @NotNull(message = "Статус блокировки обязателен")
    @Schema(description = "Заблокирован/нет", example = "true")
    private Boolean isBlocked;

    @NotNull(message = "Статус подозрительности обязателен")
    @Schema(description = "Подозрительный перевод", example = "true")
    private Boolean isSuspicious;

    @Schema(description = "Причина блокировки", example = "Сумма превышает лимит")
    private String blockedReason;

    @NotNull(message = "Статус подозрительной причины обязателен")
    @Schema(description = "Подозрительная причина", example = "Большой перевод")
    private String suspiciousReason;
}
