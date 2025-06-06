package com.bank.antifraud.util;

/**
 * Константы для маппера трансферов
 */
public class TransferMapperConstants {
    public static final boolean BLOCKED = true;
    public static final boolean NOT_BLOCKED = false;
    public static final boolean SUSPICIOUS = true;
    public static final boolean NOT_SUSPICIOUS = false;
    public static final String BLOCKED_REASON_EXCEEDED = "превышен лимит в 100_000";
    public static final String BLOCKED_REASON_NOT_EXCEEDED = "превышения лимита не обнаружено";
    public static final String SUSPICIOUS_REASON_DETECTED = "обнаружено подозрительное поведение";
    public static final String SUSPICIOUS_REASON_NOT_DETECTED = "подозрительное поведение не обнаружено";
}
