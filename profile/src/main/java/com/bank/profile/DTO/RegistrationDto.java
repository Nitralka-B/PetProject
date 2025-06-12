package com.bank.profile.DTO;

import static com.bank.profile.Utils.Constraints.COUNTRY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.CITY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.STREET_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.REGION_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.DISTRICT_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.LOCALITY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.HOUSE_NUMBER_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.HOUSE_BLOCK_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.FLAT_NUMBER_MAX_LENGTH;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegistrationDto {
    @NotNull(message = "Id не может быть null")
    private Long id;

    @Size(max = COUNTRY_MAX_LENGTH, message = "country не должно превышать 166 символов")
    @NotNull(message = "country не может быть null")
    private String country;

    @Size(max = CITY_MAX_LENGTH, message = "city не должен превышать 160 символов")
    private String city;

    @Size(max = STREET_MAX_LENGTH, message = "street не должен превышать 230 символов")
    private String street;

    @Size(max = REGION_MAX_LENGTH, message = "region не должен превышать 160 символов")
    private String region;

    @Size(max = DISTRICT_MAX_LENGTH, message = "district не должен превышать 160 символов")
    private String district;

    @Size(max = LOCALITY_MAX_LENGTH, message = "locality не должно превышать 230 символов")
    private String locality;

    @Size(max = HOUSE_NUMBER_MAX_LENGTH, message = "houseNumber не должен превышать 20 символов")
    private String houseNumber;

    @Size(max = HOUSE_BLOCK_MAX_LENGTH, message = "houseBlock не должен первышать 20 символов")
    private String houseBlock;

    @Size(max = FLAT_NUMBER_MAX_LENGTH, message = "flatNumber не должен превышать 40 символов")
    private String flatNumber;

    @NotNull(message = "index не может быть null")
    private Long index;
    private Integer column;
}
