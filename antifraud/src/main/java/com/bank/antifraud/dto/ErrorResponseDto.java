package com.bank.antifraud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс для предоставления информации об ошибке
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Метаданные для отображения ошибки")
public class ErrorResponseDto {
    @Schema(description = "Время перехвата ошибки", example = "2024-05-01T13:12:17")
    private LocalDateTime timestamp;

    @Schema(description = "Статус ошибки", example = "404")
    private int status;

    @Schema(description = "Название ошибки", example = "IllegalArgumentException")
    private String error;

    @Schema(description = "Ответ ошибки", example = "Неверно введенные данные")
    private String message;

    @Schema(description = "Путь до возникновения ошибки", example = "main/somethingMethod")
    private String path;
}
