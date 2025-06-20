package com.bank.profile.Exceptions;

import java.time.Instant;

/**
 * Формат ответа исключения для обработки в GlobalExceptionHandler
 * @param code
 * @param message
 * @param timestamp
 */
public record ErrorResponse(String code,
                            String message,
                            Instant timestamp
) { }
