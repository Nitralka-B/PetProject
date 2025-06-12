package com.bank.profile.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import static com.bank.profile.Utils.Constraints.COUNTRY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.REGION_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.CITY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.DISTRICT_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.LOCALITY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.STREET_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.HOUSE_NUMBER_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.HOUSE_BLOCK_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.FLAT_NUMBER_MAX_LENGTH;

/**
 * Сущность для регистрации
 */
@Getter
@Setter
@Entity
@Table(name = "registration", schema = "profile")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = COUNTRY_MAX_LENGTH)
    @NotNull
    @Column(name = "country", nullable = false, length = COUNTRY_MAX_LENGTH)
    private String country;

    @Size(max = REGION_MAX_LENGTH)
    @Column(name = "region", length = REGION_MAX_LENGTH)
    private String region;

    @Size(max = CITY_MAX_LENGTH)
    @Column(name = "city", length = CITY_MAX_LENGTH)
    private String city;

    @Size(max = DISTRICT_MAX_LENGTH)
    @Column(name = "district", length = DISTRICT_MAX_LENGTH)
    private String district;

    @Size(max = LOCALITY_MAX_LENGTH)
    @Column(name = "locality", length = LOCALITY_MAX_LENGTH)
    private String locality;

    @Size(max = STREET_MAX_LENGTH)
    @Column(name = "street", length = STREET_MAX_LENGTH)
    private String street;

    @Size(max = HOUSE_NUMBER_MAX_LENGTH)
    @Column(name = "house_number", length = HOUSE_NUMBER_MAX_LENGTH)
    private String houseNumber;

    @Size(max = HOUSE_BLOCK_MAX_LENGTH)
    @Column(name = "house_block", length = HOUSE_BLOCK_MAX_LENGTH)
    private String houseBlock;

    @Size(max = FLAT_NUMBER_MAX_LENGTH)
    @Column(name = "flat_number", length = FLAT_NUMBER_MAX_LENGTH)
    private String flatNumber;

    @NotNull
    @Column(name = "index", nullable = false)
    private Long index;
}
