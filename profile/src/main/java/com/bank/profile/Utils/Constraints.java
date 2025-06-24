package com.bank.profile.Utils;

public final class Constraints {
    // -------------------------------
    // Длины полей (int)
    // -------------------------------

    // Audit
    public static final int ENTITY_TYPE_MAX_LENGTH = 40;
    public static final int OPERATION_TYPE_MAX_LENGTH = 6;
    public static final int CREATED_BY_MAX_LENGTH = 255;
    public static final int MODIFIED_BY_MAX_LENGTH = 255;

    // Passport
    public static final int FIRST_NAME_MAX_LENGTH = 255;
    public static final int LAST_NAME_MAX_LENGTH = 255;
    public static final int MIDDLE_NAME_MAX_LENGTH = 255;
    public static final int GENDER_MAX_LENGTH = 3;
    public static final int BIRTH_PLACE_MAX_LENGTH = 480;

    // Principal
    public static final int USERNAME_MAX_LENGTH = 255;

    // Profile
    public static final int NAME_ON_CARD_MAX_LENGTH = 370;
    public static final int EMAIL_MAX_LENGTH = 364;

    // Registration
    public static final int COUNTRY_MAX_LENGTH = 166;
    public static final int CITY_MAX_LENGTH = 160;
    public static final int STREET_MAX_LENGTH = 230;
    public static final int DISTRICT_MAX_LENGTH = 160;
    public static final int LOCALITY_MAX_LENGTH = 230;
    public static final int HOUSE_NUMBER_MAX_LENGTH = 20;
    public static final int HOUSE_BLOCK_MAX_LENGTH = 20;
    public static final int FLAT_NUMBER_MAX_LENGTH = 40;
    public static final int REGION_MAX_LENGTH = 160;

    // -------------------------------
    // Сообщения об ошибках (String)
    // -------------------------------

    // Общие ошибки
    public static final String NULL_PARAMETERS = "OperationType and entity are null!";
    public static final String ENTITY_PARAMETERS_NULL = "Entity type, operation type and entity must not be null";
    public static final String PRINCIPAL_NULL = "Principal is null!";
    public static final String NOT_AUTHORIZED = "User not authorized";
    public static final String ENTITY_ID_NULL = "Entity id is null!";

    // Ошибки профиля
    public static final String PROFILE_DTO_NULL = "ProfileDto is null";
    public static final String PROFILE_DTO_NOT_FOUND = "profileDto not found";
    public static final String PROFILE_ID_NOT_FOUND = "profileId not found";
    public static final String PROFILE_NOT_FOUND_FORMATTED = "Profile {} not found";
    public static final String PROFILE_NOT_FOUND = "profile not found";

    private Constraints() { }
}
