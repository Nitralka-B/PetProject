package com.bank.profile.Utils;

/**
 * Enum, который содержит код исключения и ее название
 */
public enum ErrorCode {
    ENTITY_NOT_FOUND("E404", "Entity not found"),
    VALIDATION_ERROR("E400", "Validation failed"),
    ENTITY_DUPLICATE("E401", "Entity duplicate"),
    NO_AUTHORIZATION("E402", "Not authorized");

    ErrorCode(String code, String name) {
    }
}
