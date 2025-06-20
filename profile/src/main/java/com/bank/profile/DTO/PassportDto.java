package com.bank.profile.DTO;

import static com.bank.profile.Utils.Constraints.FIRST_NAME_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.LAST_NAME_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.MIDDLE_NAME_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.GENDER_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.BIRTH_PLACE_MAX_LENGTH;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class PassportDto {

    @NotNull(message = "Id обязателен для заполнения")
    private Long id;

    @NotNull(message = "series не может быть null")
    private Integer series;

    @NotNull(message = "number не может быть null")
    private Long number;

    @NotNull(message = "firstname не может быть null")
    @Size(max = FIRST_NAME_MAX_LENGTH, message = "firstname не должно превышать 255 символа")
    private String firstName;

    @NotNull(message = "lastName не может быть null")
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    @Size(max = MIDDLE_NAME_MAX_LENGTH)
    private String middleName;

    @NotNull(message = "gender не может быть null")
    @Size(max = GENDER_MAX_LENGTH, message = "gender должен содержать 3 символа")
    private String gender;

    @NotNull(message = "birthDate не может быть null")
    private LocalDate birthDate;

    @NotNull(message = "birthPlace не может быть null")
    @Size(max = BIRTH_PLACE_MAX_LENGTH, message = "birthPlace не должно превышать 480 символов")
    private String birthPlace;

    @NotNull(message = "issuedBy не может быть null")
    private String issuedBy;

    @NotNull
    private LocalDate dateOfIssue;

    @NotNull
    private Integer divisionCode;
    private LocalDate expirationDate;

    @JsonProperty("registration")
    @NotNull
    private RegistrationDto registrationDto;
}
