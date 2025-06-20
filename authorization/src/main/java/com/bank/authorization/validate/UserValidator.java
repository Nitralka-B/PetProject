package com.bank.authorization.validate;

import com.bank.authorization.entity.Role;
import com.bank.authorization.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class UserValidator {
    private static final int MIN_PASSWORD_SIZE = 8;
    private static final int MAX_PASSWORD_SIZE = 64;

    public void validateProfileId(Long profileId) {
        if (profileId == null) {
            throw new ValidationException("PROFILE_ID_REQUIRED", "ID профиля обязательно должен быть указан");
        }
        if (profileId <= 0) {
            throw new ValidationException("INVALID_PROFILE_ID", "ID профиля должен быть положительным числом или null");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("PASSWORD_REQUIRED", "Пароль обязателен");
        }
        if (password.length() < MIN_PASSWORD_SIZE || password.length() > MAX_PASSWORD_SIZE) {
            throw new ValidationException("INVALID_PASSWORD_LENGTH","Пароль должен содержать от 8 до 64 символов");
        }
    }

    public void validateRole(Role role) {
        if (role == null) {
            throw new ValidationException("ROLE_REQUIRED","Роль должна быть указана");
        }
    }
}
