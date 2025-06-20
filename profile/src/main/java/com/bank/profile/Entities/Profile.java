package com.bank.profile.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import static com.bank.profile.Utils.Constraints.NAME_ON_CARD_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.EMAIL_MAX_LENGTH;

/**
 * Сущность для профиля
 */
@Getter
@Setter
@Entity
@Table(name = "profile", schema = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Size(max = EMAIL_MAX_LENGTH)
    @Column(name = "email", length = EMAIL_MAX_LENGTH)
    private String email;

    @Size(max = NAME_ON_CARD_MAX_LENGTH)
    @Column(name = "name_on_card", length = NAME_ON_CARD_MAX_LENGTH)
    private String nameOnCard;

    @Column(name = "inn")
    private Long inn;

    @Column(name = "snils")
    private Long snils;

    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", nullable = false)
    private Passport passport;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "actual_registration_id")
    private ActualRegistration actualRegistration;
}
