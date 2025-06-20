package com.bank.authorization.dto;

import com.bank.authorization.entity.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_NOT_NULL;
import static com.bank.authorization.constants.ValidationConstants.PROFILE_ID_POSITIVE;
import static com.bank.authorization.constants.ValidationConstants.ROLE_NOT_NULL;

/**
 * DTO для запроса поиска пользователя с фильтрацией.
 * Содержит параметры для выборки:
 * - profileId (обязательный, положительный) - точный поиск по ID профиля
 * - roleFilter (обязательный) - фильтр по роли пользователя
 * Используется в API для поисковых запросов.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserGetRequest {

    @NotNull(message = PROFILE_ID_NOT_NULL)
    @Positive(message = PROFILE_ID_POSITIVE)
    private Long profileId;

    @NotNull(message = ROLE_NOT_NULL)
    private Role roleFilter;
}
