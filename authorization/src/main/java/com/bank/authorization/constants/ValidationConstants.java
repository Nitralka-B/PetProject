package com.bank.authorization.constants;

public final class ValidationConstants {

    private ValidationConstants() {}

    public static final int PASSWORD_MIN_SIZE = 8;
    public static final int PASSWORD_MAX_SIZE = 64;

    public static final String PROFILE_ID_NOT_NULL = "ID профиля обязательно должен быть указан";
    public static final String PROFILE_ID_POSITIVE = "ID профиля должен быть положительным числом";
    public static final String PASSWORD_NOT_BLANK = "Пароль обязателен";
    public static final String PASSWORD_SIZE = "Пароль должен содержать минимум 8 символов";
    public static final String ROLE_NOT_NULL = "Роль должна быть обязательна";
    public static final String LIST_USERS_NOT_NULL = "Список пользователей не может быть null";
}
