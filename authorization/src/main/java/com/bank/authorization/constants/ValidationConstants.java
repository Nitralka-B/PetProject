package com.bank.authorization.constants;

public final class ValidationConstants {

    // === Валидация профиля ===
    public static final String PROFILE_ID_NOT_NULL = "ID профиля обязательно должен быть указан";
    public static final String PROFILE_ID_POSITIVE = "ID профиля должен быть положительным числом";

    // === Валидация пароля ===
    public static final String PASSWORD_NOT_BLANK = "Пароль обязателен";
    public static final String PASSWORD_SIZE = "Пароль должен содержать минимум 8 символов";
    public static final int PASSWORD_MIN_SIZE = 8;
    public static final int PASSWORD_MAX_SIZE = 64;

    // === Валидация ролей ===
    public static final String ROLE_NOT_NULL = "Роль должна быть обязательна";
    public static final String LIST_USERS_NOT_NULL = "Список пользователей не может быть null";

    private ValidationConstants() {

    }
}
