package com.bank.antifraud.util;

/**
 * Константы для классов аудита
 */
public class AuditConstans {
    public static final String CREATE_PREFIX = "create";
    public static final String GET_ID_METHOD_NAME = "getId";
    public static final String SYSTEM = "system";
    public static final String DTO_SUFFIX = "Dto";
    public static final String IMP_SUFFIX = "ServiceImpl";
    public static final String SERVICE_SUFFIX = "Service";
    public static final String AUDIT_PATTERN = "\"id\" : ";
    public static final String ENTITY_TYPE_SUSPICIOUS_CARD_TRANSFER = "SuspiciousCardTransfer";
    public static final String ENTITY_TYPE_SUSPICIOUS_PHONE_TRANSFER = "SuspiciousPhoneTransfer";
    public static final String ENTITY_TYPE_SUSPICIOUS_ACCOUNT_TRANSFER = "SuspiciousAccountTransfer";
    public static final String UNKNOWN_ENTITY_TYPE_MSG_PREFIX = "Unknown entity type: ";

    public static final String GET_PHONE_TRANSFER_ID_METHOD_NAME = "getPhoneTransferId";
    public static final String GET_CARD_TRANSFER_ID_METHOD_NAME = "getCardTransferId";
    public static final String GET_ACCOUNT_TRANSFER_ID_METHOD_NAME = "getAccountTransferId";

}
