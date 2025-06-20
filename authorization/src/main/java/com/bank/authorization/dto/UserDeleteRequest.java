package com.bank.authorization.dto;

import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_NOT_NULL;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_POSITIVE;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для запроса на удаление пользователя.
 * Содержит обязательное поле - profileId (положительное число).
 * Используется для валидации входящих данных при удалении пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDeleteRequest {

    @NotNull(message = PROFILE_ID_NOT_NULL)
    @Positive(message = PROFILE_ID_POSITIVE)
    private Long profileId;

}
