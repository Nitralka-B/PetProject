package com.bank.publicinfo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения EntityNotFoundException.
     * @param ex исключение
     * @return ответ с кодом 404 и информацией об ошибке
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    /**
     * Обрабатывает исключения ValidationException.
     * @param ex исключение
     * @return ответ с кодом 400 и информацией об ошибке
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_FAILED", ex.getMessage()));
    }

    /**
     * Обрабатывает исключения IllegalArgumentException.
     * @param ex исключение
     * @return ответ с кодом 400 и информацией об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("INVALID_ARGUMENT", ex.getMessage()));
    }

    /**
     * Запись для ответа с информацией об ошибке.
     * @param code код ошибки
     * @param message сообщение об ошибке
     */
    public record ErrorResponse(String code, String message) {}
}