package com.bank.authorization.dto;

import com.bank.authorization.entity.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для передачи данных о пользователе между слоями приложения.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDto {

    private Long id;

    private Long profileId;

    private Role role;

}
