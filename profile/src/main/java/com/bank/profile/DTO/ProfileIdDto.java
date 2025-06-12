package com.bank.profile.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ProfileIdDto {
    @NotNull(message = "Id обязателен для заполнения")
    private Long id;
}
