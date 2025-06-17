package com.bank.publicinfo.testutil;

public final class TestConstants {
    public static final Long TEST_ID = 1L;
    public static final Long TEST_ID_2 = 2L;
    public static final String TEST_METHOD_SIGNATURE = "testMethod";
    public static final Long TEST_BIK = 123456789L;
    public static final Long TEST_OLD_BIK = 987654321L;
    public static final Long TEST_OLD_INN = 9876543210L;
    public static final Long TEST_INN = 1234567890L;
    public static final Long TEST_KPP = 123456789L;
    public static final Integer TEST_COR_ACCOUNT = 123456;
    public static final String TEST_CITY = "Moscow";
    public static final String TEST_COMPANY_TYPE = "AO";
    public static final String TEST_BANK_NAME = "Sberbank";
    public static final String TEST_USER = "testUser";
    public static final String SYSTEM_USER = "system";
    public static final String SERIALIZED_BANK_DETAILS = "{\"id\":1,\"bik\":123456789}";
    public static final String ENTITY_TYPE = "BankDetails";
    public static final String CREATE_OPERATION = "CREATE";
    public static final String UPDATE_OPERATION = "UPDATE";
    public static final String DELETE_OPERATION = "DELETE";
    public static final String TEST_EXCEPTION_MESSAGE = "DB error";
    public static final String AUDIT_LOG_FAILED_MESSAGE = "Audit log failed";
    // Kafka Configuration
    public static final int KAFKA_PARTITIONS = 3;
    public static final short KAFKA_REPLICAS = 1;
    public static final long KAFKA_DEFAULT_RETENTION_MS = 604800000L;
    public static final long KAFKA_SHORT_RETENTION_MS = 3600000L;
    public static final String BANK_CREATE_TOPIC = "public-info.bank.create";
    public static final String BANK_UPDATE_TOPIC = "public-info.bank.update";
    public static final String BANK_DELETE_TOPIC = "public-info.bank.delete";
    public static final String BANK_GET_TOPIC = "public-info.bank.get";
    public static final String BANK_RESPONSE_TOPIC = "public-info.bank.response";
    public static final String PARTITIONS_FIELD = "partitions";
    public static final String REPLICAS_FIELD = "replicas";
    public static final String DEFAULT_RETENTION_MS_FIELD = "defaultRetentionMs";
    public static final String SHORT_RETENTION_MS_FIELD = "shortRetentionMs";
    public static final String BANK_CREATE_TOPIC_FIELD = "bankCreateTopic";
    public static final String BANK_UPDATE_TOPIC_FIELD = "bankUpdateTopic";
    public static final String BANK_DELETE_TOPIC_FIELD = "bankDeleteTopic";
    public static final String BANK_GET_TOPIC_FIELD = "bankGetTopic";
    public static final String BANK_RESPONSE_TOPIC_FIELD = "bankResponseTopic";
    public static final String RETENTION_MS_CONFIG = "retention.ms";
    public static final String BUILD_BANK_TOPIC_METHOD = "buildBankTopic";
    public static final String CONSUMER_CREATION_SHOULD_NOT_THROW = "Consumer creation and closing should not throw exceptions";
    public static final String CONSUMER_FACTORY_SHOULD_NOT_BE_NULL = "Consumer factory should not be null";
    public static final String BANK_DETAILS_LISTENER_FACTORY_SHOULD_BE_CONFIGURED = "BankDetails listener container factory should be configured";
    public static final String BANK_GET_LISTENER_FACTORY_SHOULD_BE_CONFIGURED = "BankGet listener container factory should be configured";
    public static final String DEFAULT_ERROR_HANDLER_SHOULD_BE_CONFIGURED = "Default error handler should be configured";
    public static final String BOOTSTRAP_SERVERS_SHOULD_MATCH = "Bootstrap servers should match configured value";
    public static final String GROUP_ID_SHOULD_MATCH = "Group ID should match configured value";
    public static final String AUTO_OFFSET_RESET_SHOULD_MATCH = "Auto offset reset should match configured value";
    public static final String AUTO_COMMIT_SHOULD_BE_DISABLED = "Auto commit should be disabled";
    public static final String MAX_POLL_INTERVAL_SHOULD_MATCH = "Max poll interval should match configured value";
    public static final String SESSION_TIMEOUT_SHOULD_MATCH = "Session timeout should match configured value";
    public static final String ACK_MODE_SHOULD_MATCH = "Ack mode should match configured value";
    public static final String COMMON_CONSUMER_PROPS_METHOD = "commonConsumerProps";
    public static final String BOOTSTRAP_SERVERS_FIELD = "bootstrapServers";
    // Alternative values for testing
    public static final int KAFKA_PARTITIONS_ALT = 5;
    public static final short KAFKA_REPLICAS_ALT = 2;
    public static final long KAFKA_DEFAULT_RETENTION_MS_ALT = 86400000L;
    public static final String TEST_TOPIC_NAME = "test.topic";
    // Kafka Consumer Configuration
    public static final String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String KAFKA_GROUP_ID = "public-info-group";
    public static final String KAFKA_AUTO_OFFSET_RESET = "earliest";
    public static final String KAFKA_ACK_MODE = "MANUAL_IMMEDIATE";
    public static final int KAFKA_MAX_POLL_INTERVAL_MS = 300000;
    public static final int KAFKA_SESSION_TIMEOUT_MS = 10000;
    // Аннотации и проверки
    public static final String ENTITY_ANNOTATION_SHOULD_BE_PRESENT = "Entity annotation should be present";
    public static final String TABLE_ANNOTATION_SHOULD_NOT_BE_NULL = "Table annotation should not be null";
    public static final String AUDIT_TABLE_NAME = "audit";
    public static final String TABLE_NAME_SHOULD_MATCH_AUDIT = "Table name should match 'audit'";
    public static final String ID_ANNOTATION_SHOULD_BE_PRESENT = "Id annotation should be present";
    public static final String GENERATED_VALUE_ANNOTATION_SHOULD_NOT_BE_NULL = "GeneratedValue annotation should not be null";
    public static final String GENERATION_STRATEGY_SHOULD_BE_IDENTITY = "Generation strategy should be IDENTITY";
    public static final String COLUMN_ANNOTATION_SHOULD_NOT_BE_NULL_FORMAT = "Column annotation for field %s should not be null";
    public static final String FIELD_LENGTH_SHOULD_MATCH_FORMAT = "Field %s should have length %d";
    public static final String FIELD_LENGTH_SHOULD_MATCH_CONSTANT_FORMAT = "Field %s length should match constant %s";
    public static final String FIELD_NULLABLE_SHOULD_MATCH_FORMAT = "Field %s nullable should be %b";
    // Имена полей
    public static final String ID_FIELD_NAME = "id";
    public static final String ENTITY_TYPE_FIELD_NAME = "entityType";
    public static final String OPERATION_TYPE_FIELD_NAME = "operationType";
    public static final String CREATED_BY_FIELD_NAME = "createdBy";
    public static final String MODIFIED_BY_FIELD_NAME = "modifiedBy";
    public static final String CREATED_AT_FIELD_NAME = "createdAt";
    public static final String MODIFIED_AT_FIELD_NAME = "modifiedAt";
    public static final String NEW_ENTITY_JSON_FIELD_NAME = "newEntityJson";
    public static final String ENTITY_JSON_FIELD_NAME = "entityJson";
    // Lombok проверки
    public static final String TOSTRING_SHOULD_NOT_BE_NULL = "toString() should not be null";
    public static final String ID_SHOULD_MATCH = "ID should match";
    public static final String ENTITY_TYPE_SHOULD_MATCH = "Entity type should match";
    public static final String OPERATION_TYPE_SHOULD_MATCH = "Operation type should match";
    public static final String CREATED_BY_SHOULD_MATCH = "Created by should match";
    public static final String MODIFIED_BY_SHOULD_MATCH = "Modified by should match";
    public static final String CREATED_AT_SHOULD_MATCH = "Created at should match";
    public static final String MODIFIED_AT_SHOULD_MATCH = "Modified at should match";
    public static final String NEW_ENTITY_JSON_SHOULD_MATCH = "New entity JSON should match";
    public static final String ENTITY_JSON_SHOULD_MATCH = "Entity JSON should match";

    // equals/hashCode проверки
    public static final String EQUALS_SHOULD_BE_TRUE_FOR_SAME_OBJECTS = "Equals should be true for same objects";
    public static final String EQUALS_SHOULD_BE_FALSE_FOR_DIFFERENT_OBJECTS = "Equals should be false for different objects";
    public static final String HASHCODE_SHOULD_BE_EQUAL_FOR_SAME_OBJECTS = "Hashcode should be equal for same objects";
    public static final String HASHCODE_SHOULD_BE_DIFFERENT_FOR_DIFFERENT_OBJECTS = "Hashcode should be different for different objects";
    public static final String EQUALS_SHOULD_BE_FALSE_FOR_NULL = "Equals should be false for null";
    public static final String EQUALS_SHOULD_BE_FALSE_FOR_DIFFERENT_CLASS = "Equals should be false for different class";
    public static final String EQUALS_SHOULD_BE_REFLEXIVE = "Equals should be reflexive";
    public static final String EQUALS_SHOULD_BE_SYMMETRIC = "Equals should be symmetric";
    public static final String EQUALS_SHOULD_BE_TRANSITIVE = "Equals should be transitive";
    // Alternative values for testing
    public static final Long TEST_ALT_BIK = 987654321L;
    public static final Long TEST_ALT_INN = 9876543210L;
    public static final Long TEST_ALT_KPP = 987654321L;
    public static final Integer TEST_ALT_COR_ACCOUNT = 987654321;
    public static final String TEST_ALT_CITY = "London";
    public static final String TEST_ALT_COMPANY_TYPE = "OOO";
    public static final String TEST_ALT_BANK_NAME = "Tinkoff";
    // Field length constraints
    public static final int MAX_CITY_LENGTH = 180;
    public static final int MAX_COMPANY_TYPE_LENGTH = 15;
    public static final int MAX_BANK_NAME_LENGTH = 80;
    public static final String TEST_LONG_STRING = "a";
    // Assertion messages
    public static final String BIK_SHOULD_MATCH = "BIK should match";
    public static final String INN_SHOULD_MATCH = "INN should match";
    public static final String KPP_SHOULD_MATCH = "KPP should match";
    public static final String COR_ACCOUNT_SHOULD_MATCH = "Correspondent account should match";
    public static final String CITY_SHOULD_MATCH = "City should match";
    public static final String COMPANY_TYPE_SHOULD_MATCH = "Company type should match";
    public static final String BANK_NAME_SHOULD_MATCH = "Bank name should match";
    public static final String ENTITY_SHOULD_NOT_BE_NULL = "Entity should not be null";
    public static final String NO_VIOLATIONS_EXPECTED = "No validation violations expected";
    public static final String UNIQUE_CONSTRAINTS_REQUIRE_DB = "Unique constraints testing requires database integration";
    public static final String TOSTRING_SHOULD_CONTAIN_BANK_NAME = "toString() should contain bank name";
    public static final String TOSTRING_SHOULD_CONTAIN_CITY = "toString() should contain city";
    // Exception messages
    public static final String ENTITY_NOT_FOUND_FORMAT = "%s ID: %d";
    public static final String TEST_MESSAGE = "Test message";
    public static final String NOT_FOUND_CODE = "NOT_FOUND";
    public static final String VALIDATION_FAILED_CODE = "VALIDATION_FAILED";
    public static final String INVALID_ARGUMENT_CODE = "INVALID_ARGUMENT";
    public static final String ILLEGAL_ARGUMENT_MESSAGE = "Invalid argument";
    public static final String TEST_ERROR_CODE = "TEST_CODE";
    public static final String TEST_ERROR_MESSAGE = "Test message";
    public static final String EXCEPTION_SHOULD_NOT_BE_NULL = "Exception should not be null";
    public static final String MESSAGE_SHOULD_MATCH_INPUT = "Exception message should match the input message";
    public static final String SHOULD_BE_SUBCLASS_OF_RUNTIME_EXCEPTION = "ValidationException should be a subclass of RuntimeException";
    public static final String MESSAGE_SHOULD_BE_NULL_WHEN_CONSTRUCTED_WITH_NULL = "Exception message should be null when constructed with null";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation failed";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found";
    public static final String KEY_SERIALIZER_SHOULD_BE_STRING = "Key serializer should be StringSerializer";
    public static final String VALUE_SERIALIZER_SHOULD_BE_JSON = "Value serializer should be JsonSerializer";
    public static final String TYPE_MAPPINGS_SHOULD_BE_CONFIGURED = "Type mappings should be configured";
    public static final String PRODUCER_FACTORY_SHOULD_NOT_BE_NULL = "Producer factory should not be null";
    public static final String KAFKA_TEMPLATE_SHOULD_NOT_BE_NULL = "KafkaTemplate should not be null";
    public static final String OTHER_USER = "otherUser";
    public static final String OTHER_MODIFIER = "otherModifier";
    public static final String TIME_SHOULD_BE_PLUS_ONE_HOUR = "Time should be plus one hour";
    // Bank ID formats
    public static final String VALID_BANK_ID_STRING = "1";
    public static final String INVALID_BANK_ID_STRING = "abc";
    public static final String BANK_ID_WITH_SPECIAL_CHARS = "!@#123$%^";
    public static final String BANK_ID_WITH_HYPHENS = "123-456";
    public static final String ONLY_SPECIAL_CHARS = "!@#$%^";
    // Response messages
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String INVALID_ID_FORMAT_MESSAGE = "Invalid bank ID format";
    public static final String SERVICE_ERROR_MESSAGE = "Database error";
    // UUID для тестирования
    public static final String TEST_UUID = "123e4567-e89b-12d3-a456-426614174000";
    // Заголовки сообщений
    public static final String MESSAGE_TYPE_HEADER = "message-type";
    public static final String RESPONSE_MESSAGE_TYPE = "response";
    // JSON значения
    public static final String EMPTY_JSON = "{}";
    public static final String LICENSE_JSON = "{\"number\":\"123\"}";
    public static final String OLD_LICENSE_JSON = "{\"number\":\"122\"}";
    // Типы сущностей
    public static final String LICENSE_ENTITY_TYPE = "License";
    public static final String TYPE1_ENTITY_TYPE = "Type1";
    public static final String TYPE2_ENTITY_TYPE = "Type2";
    // Invalid IDs
    public static final Long NULL_ID = null;
    public static final Long ZERO_ID = 0L;
    public static final Long NEGATIVE_ID = -1L;
    // Exception messages
    public static final String INVALID_ID_MESSAGE = "ID не может быть null или отрицательным";
    // Validation messages
    public static final String BIK_ALREADY_EXISTS_MESSAGE = "Bank with BIK %d already exists";
    public static final String INN_ALREADY_EXISTS_MESSAGE = "Bank with INN %d already exists";
    public static final String BIK_BELONGS_TO_ANOTHER_MESSAGE = "BIK %d belongs to another bank";
    public static final String INN_BELONGS_TO_ANOTHER_MESSAGE = "INN %d belongs to another bank";
    public static final String DTO_CANNOT_BE_NULL_MESSAGE = "BankDetailsDto cannot be null";
    public static final String BIK_CANNOT_BE_NULL_MESSAGE = "BIK cannot be null";
    public static final String INN_CANNOT_BE_NULL_MESSAGE = "INN cannot be null";
    // Serialization messages
    public static final String SERIALIZATION_ERROR_MESSAGE = "Failed to serialize";
    public static final String DESERIALIZATION_ERROR_MESSAGE = "Failed to deserialize";
    public static final String INVALID_INPUT_MESSAGE = "Invalid input";
    // JSON and serialization constants
    public static final String TEST_JSON = "{\"field\":\"test\"}";
    public static final String TEST_VALUE = "test";
    // Time deltas
    public static final int MODIFIED_AT_DELTA_MINUTES = 30;
    public static final int CREATED_AT_DELTA_MINUTES = 15;
    // Field names for assertions
    public static final String AUDIT_DTO_ID_FIELD = "id";
    public static final String AUDIT_DTO_ENTITY_TYPE_FIELD = "entityType";
    // Collection sizes
    public static final int TEST_LIST_SIZE = 2;
    public static final int SINGLE_SAVE_OPERATION = 1;
    public static final String LOG_SAVING_BANK_DETAILS = "Saving bank details";
    public static final String LOG_INN_PREFIX = "inn=";
    public static final String AUDIT_ENTITY_TYPE = "Audit";
    public static final String OPERATION_SHOULD_NOT_THROW = "Operation should not throw exception";
    public static final String EXCEPTION_MESSAGE_SHOULD_MATCH = "Exception message should match";
    public static final String ENTITY_SHOULD_BE_NULL = "Entity should be null";
    public static final String DTO_SHOULD_NOT_BE_NULL = "DTO should not be null";
    public static final String DTO_SHOULD_MATCH_EXPECTED = "DTO should match expected";
    public static final String RESULT_LIST_SHOULD_NOT_BE_NULL = "Result list should not be null";
    public static final String LIST_SIZE_SHOULD_MATCH = "List size should match";
    public static final String RESULT_LIST_SHOULD_MATCH_EXPECTED = "Result list should match expected";
    // Collection sizes
    public static final int SINGLE_ELEMENT_LIST_SIZE = 1;
    // Общие сообщения об ошибках
    public static final String ENTITY_SAVE_FAILED_FORMAT = "Failed to save %s record";
    public static final String DB_ERROR_MESSAGE = "Database error occurred";
    // Общие проверочные сообщения
    public static final String SHOULD_NOT_BE_NULL_FORMAT = "%s should not be null";
    public static final String SHOULD_MATCH_FORMAT = "%s should match expected value";
    public static final String SHOULD_THROW_EXCEPTION = "Should throw exception";
    public static final String ENTITY_NOT_FOUND_WITH_ID_FORMAT = "%s not found with id: %d ID: %d";
}
