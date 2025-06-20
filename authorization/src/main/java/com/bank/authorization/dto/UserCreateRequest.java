package com.bank.authorization.dto;

import static com.bank.authorization.constants.ValidationConstants.PASSWORD_MAX_SIZE;
import static com.bank.authorization.constants.ValidationConstants.PASSWORD_MIN_SIZE;
import static com.bank.authorization.constants.ValidationConstants.PASSWORD_NOT_BLANK;
import static com.bank.authorization.constants.ValidationConstants.PASSWORD_SIZE;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_NOT_NULL;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_POSITIVE;
import static com.bank.authorization.constants.ValidationConstants.ROLE_NOT_NULL;
import com.bank.authorization.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO-класс для запроса на создание пользователя.
 * Содержит валидируемые поля, необходимые для регистрации:
 * - ID профиля (положительное число)
 * - Пароль (длина 8-64 символа)
 * - Роль (обязательное поле)
 */
@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequest {

    @NotNull(message = PROFILE_ID_NOT_NULL)
    @Positive(message = PROFILE_ID_POSITIVE)
    private Long profileId;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE)
    private String password;

    @NotNull(message = ROLE_NOT_NULL)
    private Role role;
}
