package com.bank.antifraud.util;

/**
 * Константы для конфигурации Kafka, сообщений и топиков
 */
public class KafkaConstants {

    public static final String ACCOUNT = "ACCOUNT";
    public static final String CARD = "CARD";
    public static final String PHONE = "PHONE";
    public static final String BLOCKED = "BLOCKED";
    public static final String ALLOWED = "ALLOWED";

    public static final String UNKNOWN = "Неизвестный тип перевода: ";
    public static final String EMPTY_PARAMS_AUDIT = "Исходящие параметры аудита не могут быть пустыми: {}";
    public static final String EMPTY_PARAMS_TRANSFER = "Исходящие параметры перевода не могут быть пустыми: {}, {}, {}";
    public static final String SEND_RESULT = "Отправлен результат: {}";
    public static final String SUCCESSFUL = "Успешно отправлено в {}";
    public static final String ERROR_SENDING = "Ошибка отправки в {}";
    public static final String CRITICAL_ERROR = "Критическая ошибка при отправке в {}";
    public static final String EMPTY_MESSAGE = "Пустое сообщение для топика {}";
    public static final String AUDIT_ERROR = "Ошибка отправки аудита: {}";
    public static final String AUDIT_SENT = "Отправлен AuditDto из AuditProducer: {}";
    public static final String EMPTY_RECORD = "Получена пустая запись аудита";
    public static final String EMPTY_RECORD_ERROR = "Запись аудита пустая";
    public static final String AUDIT_RECEIVED = "Получен AuditDto: {}";
    public static final String AUDIT_PROCESS_ERROR = "Ошибка при обработке аудита: {}";
    public static final String UNKNOWN_TOPIC = "Неизвестный топик: {}";
    public static final String TOPIC_ERROR = "Ошибка при получении топиков: {}";
    public static final String TOPIC_ERROR_MSG = "Ошибка получения записи из топиков CREATE/UPDATE";
    public static final String TRANSFER_ERROR = "Ошибка при {} перевода типа {}: {}";
    public static final String TOPIC_RECEIVED = "Получено значение из топика {}: {}";
    public static final String NULL_TRANSFER_OBJECT = "Получен null объект TransferChecked для типа {}.";
    public static final String NULL_AMOUNT = "Получен перевод с нулевой суммой (amount is null).";
    public static final String NULL_AMOUNT_ERROR = "Сумма перевода не может быть null.";

    public static final String ACCOUNT_TOPIC = "transfer.account";
    public static final String CARD_TOPIC = "transfer.card";
    public static final String PHONE_TOPIC = "transfer.phone";
    public static final String TRANSFER_GROUP = "transfer-group";
    public static final String AUDIT_EVENTS = "audit-events";
    public static final String TRANSFER_VERDICT = "transfers.verdict";
    public static final String AUDIT_GROUP = "anti-fraud-group";

    public static final String SUSPICIOUS_CREATE = "suspicious-transfers.create";
    public static final String SUSPICIOUS_UPDATE = "suspicious-transfers.update";
    public static final String SUSPICIOUS_DELETE = "suspicious-transfers.delete";
    public static final String SUSPICIOUS_GET = "suspicious-transfers.get";

    public static final String TYPE_CREATE = "создании";
    public static final String TYPE_UPDATE = "обновлении";
}
