package com.bank.authorization.dto;

import com.bank.authorization.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.bank.authorization.constants.ValidationConstants.PASSWORD_MAX_SIZE;
import static com.bank.authorization.constants.ValidationConstants.PASSWORD_MIN_SIZE;
import static com.bank.authorization.constants.ValidationConstants.PASSWORD_NOT_BLANK;
import static com.bank.authorization.constants.ValidationConstants.PASSWORD_SIZE;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_NOT_NULL;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_POSITIVE;
import static com.bank.authorization.constants.ValidationConstants.ROLE_NOT_NULL;

/**
 * DTO для запроса обновления пользователя.
 * Содержит все поля, которые могут быть изменены:
 * - profileId (идентификатор для поиска пользователя)
 * - password (новый пароль с валидацией)
 * - role (новая роль пользователя)
 * Обеспечивает валидацию данных перед обновлением.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotNull(message = PROFILE_ID_NOT_NULL)
    @Positive(message = PROFILE_ID_POSITIVE)
    private Long profileId;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Size(min = PASSWORD_MIN_SIZE, max = PASSWORD_MAX_SIZE, message = PASSWORD_SIZE)
    private String password;

    @NotNull(message = ROLE_NOT_NULL)
    private Role role;

}
