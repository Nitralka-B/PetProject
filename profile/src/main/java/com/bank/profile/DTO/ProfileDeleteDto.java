package com.bank.profile.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ProfileDeleteDto {
    @NotNull(message = "Id обязателен для заполнения")
    private Long id;
}
