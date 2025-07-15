package com.bank.profile.Utils;

import com.bank.profile.DTO.ActualRegistrationDto;
import com.bank.profile.DTO.PassportDto;
import com.bank.profile.DTO.PrincipalUserDto;
import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.DTO.RegistrationDto;
import com.bank.profile.Entities.ActualRegistration;
import com.bank.profile.Entities.Passport;
import com.bank.profile.Entities.Profile;
import com.bank.profile.Entities.Registration;

import java.time.LocalDate;

public class TestUtils {
    //Profile
    public static ProfileDto getProfileForCreate() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setPhoneNumber(89256164948L);
        profileDto.setPassportDto(getPassportDtoForProfile());
        return profileDto;
    }

    public static ProfileDto getProfileDtoForUpdate() {
        ProfileDto profileDto = getProfileForCreate();
        profileDto.setId(1234L);
        return profileDto;
    }

    public static Profile getProfileForUpdate() {
        Profile profile = new Profile();
        profile.setId(1234L);
        profile.setPhoneNumber(89256164948L);
        profile.setPassport(getPassportForProfile());

        return profile;
    }

    public static Profile getProfileForDelete() {
        Profile profile = getProfileForUpdate();

        return profile;
    }

    public static Profile getProfile() {
        Profile profile = getProfileForUpdate();

        return profile;
    }

    public static ProfileDto getProfileDto() {
        ProfileDto profileDto = getProfileDtoForUpdate();
        return profileDto;
    }

    public static PassportDto getPassportDtoForProfile() {
        PassportDto passportDto = new PassportDto();
        passportDto.setSeries(1298);
        passportDto.setNumber(3298760741L);
        passportDto.setLastName("Tester");
        passportDto.setFirstName("Tester");
        passportDto.setGender("MAN");
        passportDto.setBirthDate(LocalDate.parse("2004-07-29"));
        passportDto.setBirthPlace("Питер, Россия");
        passportDto.setIssuedBy("ГУ МВД по Тверской области");
        passportDto.setDateOfIssue(LocalDate.parse("2018-10-20"));
        passportDto.setDivisionCode(946018);
        passportDto.setRegistrationDto(getRegistrationDtoForPassport());

        return passportDto;
    }

    public static RegistrationDto getRegistrationDtoForPassport() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setCountry("Russia");
        registrationDto.setIndex(123454L);

        return registrationDto;
    }

    public static Passport getPassportForProfile() {
        Passport passport = new Passport();
        passport.setSeries(1298);
        passport.setNumber(3298760741L);
        passport.setLastName("Tester");
        passport.setFirstName("Tester");
        passport.setGender("MAN");
        passport.setBirthDate(LocalDate.parse("2004-07-29"));
        passport.setBirthPlace("Питер, Россия");
        passport.setIssuedBy("ГУ МВД по Тверской области");
        passport.setDateOfIssue(LocalDate.parse("2018-10-20"));
        passport.setDivisionCode(946018);
        passport.setRegistration(getRegistrationForPassport());

        return passport;
    }

    public static Registration getRegistrationForPassport() {
        Registration registration = new Registration();
        registration.setCountry("Russia");
        registration.setIndex(123454L);

        return registration;
    }

    //ActualRegistration
    public static ActualRegistrationDto getActualRegistrationDtoForCreate() {
        ActualRegistrationDto actualRegistrationDto = new ActualRegistrationDto();
        actualRegistrationDto.setCountry("Russia");
        actualRegistrationDto.setIndex(123454L);

        return actualRegistrationDto;
    }

    public static ActualRegistration getActualRegistrationForCreate() {
        ActualRegistration actualRegistration = new ActualRegistration();
        actualRegistration.setCountry("Russia");
        actualRegistration.setIndex(123454L);

        return actualRegistration;
    }

    public static ActualRegistrationDto getActualRegistrationDtoForUpdate() {
        ActualRegistrationDto actualRegistrationDto = getActualRegistrationDtoForCreate();
        actualRegistrationDto.setId(12345L);

        return actualRegistrationDto;
    }

    public static ActualRegistration getActualRegistrationForUpdate() {
        ActualRegistration actualRegistration = getActualRegistrationForCreate();
        actualRegistration.setId(12345L);

        return actualRegistration;
    }

    public static ActualRegistrationDto getActualRegistrationDtoForDelete() {
        ActualRegistrationDto actualRegistrationDto = getActualRegistrationDtoForUpdate();

        return actualRegistrationDto;
    }

    public static ActualRegistration getActualRegistrationForDelete() {
        ActualRegistration actualRegistration = getActualRegistrationForUpdate();

        return actualRegistration;
    }

    public static ActualRegistrationDto getActualRegistrationDtoForGet() {
        ActualRegistrationDto actualRegistrationDto = getActualRegistrationDtoForUpdate();

        return actualRegistrationDto;
    }

    public static ActualRegistration getActualRegistrationForGet() {
        ActualRegistration actualRegistration = getActualRegistrationForUpdate();

        return actualRegistration;
    }

    //Passport
    public static Passport getPassportForCreate() {
        Passport passport = getPassportForProfile();

        return passport;
    }

    public static PassportDto getPassportDtoForCreate() {
        PassportDto passportDto = getPassportDtoForProfile();

        return passportDto;
    }

    public static PassportDto getPassportDtoForUpdate() {
        PassportDto passportDto = getPassportDtoForCreate();
        passportDto.setId(12345L);

        return passportDto;
    }

    public static Passport getPassportForUpdate() {
        Passport passport = getPassportForCreate();
        passport.setId(12345L);

        return passport;
    }

    public static Passport getPassportForDelete() {
        Passport passport = getPassportForUpdate();
        return passport;
    }

    public static PassportDto getPassportDtoForDelete() {
        PassportDto passportDto = getPassportDtoForUpdate();
        return passportDto;
    }

    public static PassportDto getPassportDtoForGet() {
        PassportDto passportDto = getPassportDtoForUpdate();

        return passportDto;
    }

    public static Passport getPassportForGet() {
        Passport passport = getPassportForUpdate();

        return passport;
    }

    //Registration
    public static Registration getRegistrationForCreate() {
        Registration registration = getRegistrationForPassport();

        return registration;
    }

    public static RegistrationDto getRegistrationDtoForCreate() {
        RegistrationDto registrationDto = getRegistrationDtoForPassport();

        return registrationDto;
    }

    public static RegistrationDto getRegistrationDtoForUpdate() {
        RegistrationDto registrationDto = getRegistrationDtoForCreate();
        registrationDto.setId(12345L);

        return registrationDto;
    }

    public static Registration getRegistrationForUpdate() {
        Registration registration = getRegistrationForCreate();
        registration.setId(12345L);

        return registration;
    }

    public static RegistrationDto getRegistrationDtoForDelete() {
        RegistrationDto registrationDto = getRegistrationDtoForUpdate();

        return registrationDto;
    }

    public static Registration getRegistrationForDelete() {
        Registration registration = getRegistrationForUpdate();

        return registration;
    }

    public static RegistrationDto getRegistrationDtoForGet() {
        RegistrationDto registrationDto = getRegistrationDtoForUpdate();

        return registrationDto;
    }

    public static Registration getRegistrationForGet() {
        Registration registration = getRegistrationForUpdate();

        return registration;
    }

    //AccountDetails
    public static Profile getProfileForAccountDetails() {
        Profile profile = getProfile();
        profile.setId(1L);
        return profile;
    }

    //Audit
    public static PrincipalUserDto getPrincipal() {
        PrincipalUserDto principal = new PrincipalUserDto();
        principal.setUsername("TestUser");
        return principal;
    }
}
