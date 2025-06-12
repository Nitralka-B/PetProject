package com.bank.profile.DTO;

import com.bank.profile.Entities.ActualRegistration;
import static com.bank.profile.Utils.Constraints.NAME_ON_CARD_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.EMAIL_MAX_LENGTH;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProfileDto {
    @NotNull(message = "Id не может быть null")
    private Long id;

    @NotNull(message = "phoneNumber не может быть null")
    private Long phoneNumber;

    @Size(max = NAME_ON_CARD_MAX_LENGTH, message = "nameOnCard не должно превышать 370 символов")
    private String nameOnCard;

    @Size(max = EMAIL_MAX_LENGTH, message = "email не должен превышать 364 символа")
    private String email;

    private Long inn;
    private Long snils;

    @JsonProperty("passport")
    @NotNull(message = "passportDto не может быть null")
    private PassportDto passportDto;
    private ActualRegistration actualRegistration;
}
