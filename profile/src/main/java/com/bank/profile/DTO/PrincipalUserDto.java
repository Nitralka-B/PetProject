package com.bank.profile.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.bank.profile.Utils.Constraints.USERNAME_MAX_LENGTH;

@Data
@Getter
@Setter
public class PrincipalUserDto {

    @NotNull(message = "Id обязателен для заполнения")
    private Long id;

    @Size(max = USERNAME_MAX_LENGTH, message = "username не должен превышать 255 символа")
    private String username;
}
