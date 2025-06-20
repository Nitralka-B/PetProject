package com.bank.authorization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static com.bank.authorization.constants.ValidationConstants.LIST_USERS_NOT_NULL;
import java.util.List;

/**
 * DTO-обертка для возвращаемого списка пользователей в API.
 * Содержит валидируемый список UserDto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGetResponse {

    @NotNull(message = LIST_USERS_NOT_NULL)
    private List<UserDto> users;

}
