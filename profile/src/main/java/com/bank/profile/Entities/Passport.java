package com.bank.profile.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import static com.bank.profile.Utils.Constraints.FIRST_NAME_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.LAST_NAME_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.MIDDLE_NAME_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.GENDER_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.BIRTH_PLACE_MAX_LENGTH;

import java.time.LocalDate;

/**
 * Сущность для паспорта
 */
@Getter
@Setter
@Entity
@Table(name = "passport", schema = "profile")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "series", nullable = false)
    private Integer series;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @Size(max = LAST_NAME_MAX_LENGTH)
    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = FIRST_NAME_MAX_LENGTH)
    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = MIDDLE_NAME_MAX_LENGTH)
    @Column(name = "middle_name")
    private String middleName;

    @Size(max = GENDER_MAX_LENGTH)
    @NotNull
    @Column(name = "gender", nullable = false, length = GENDER_MAX_LENGTH)
    private String gender;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Size(max = BIRTH_PLACE_MAX_LENGTH)
    @NotNull
    @Column(name = "birth_place", nullable = false, length = BIRTH_PLACE_MAX_LENGTH)
    private String birthPlace;

    @NotNull
    @Column(name = "issued_by", nullable = false, length = Integer.MAX_VALUE)
    private String issuedBy;

    @NotNull
    @Column(name = "date_of_issue", nullable = false)
    private LocalDate dateOfIssue;

    @NotNull
    @Column(name = "division_code", nullable = false)
    private Integer divisionCode;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;
}
