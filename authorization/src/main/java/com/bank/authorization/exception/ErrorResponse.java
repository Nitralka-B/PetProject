package com.bank.authorization.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private String message;
    private Instant timestamp;
    private Map<String, String> details;

    public ErrorResponse(String code, String message, Instant timestamp) {
        this(code, message, timestamp, null);
    }
}
