package com.bank.profile.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.bank.profile.Utils.Constraints.CITY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.COUNTRY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.DISTRICT_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.FLAT_NUMBER_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.HOUSE_BLOCK_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.HOUSE_NUMBER_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.LOCALITY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.REGION_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.STREET_MAX_LENGTH;

@Getter
@Setter
public class ActualRegistrationDto {

    @NotNull
    private Long id;

    @Size(max = COUNTRY_MAX_LENGTH)
    @NotNull
    private String country;

    @Size(max = REGION_MAX_LENGTH)
    private String region;

    @Size(max = CITY_MAX_LENGTH)
    private String city;

    @Size(max = DISTRICT_MAX_LENGTH)
    private String district;

    @Size(max = LOCALITY_MAX_LENGTH)
    private String locality;

    @Size(max = STREET_MAX_LENGTH)
    private String street;

    @Size(max = HOUSE_NUMBER_MAX_LENGTH)
    private String houseNumber;

    @Size(max = HOUSE_BLOCK_MAX_LENGTH)
    private String houseBlock;

    @Size(max = FLAT_NUMBER_MAX_LENGTH)
    private String flatNumber;

    @NotNull
    private Long index;
}
