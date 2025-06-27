package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Very light-weight unit test: verifies that handler methods are chosen correctly and no exception is thrown
 * in normal flow. Since methods only log, we just ensure absence of exceptions.
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SuspiciousTransferConsumerTest {

    private final SuspiciousTransferConsumer consumer = new SuspiciousTransferConsumer();

    private static final int PARTITION = 0;
    private static final long OFFSET = 0L;
    private static final String PAYLOAD_CREATE = "create";
    private static final String PAYLOAD_UPDATE = "update";
    private static final String PAYLOAD_DELETE = "delete";
    private static final String PAYLOAD_GET = "get";
    private static final String TOPIC_UNKNOWN = "UNKNOWN";
    private static final String PAYLOAD_UNKNOWN = "unknownPayload";
    private static final String KEY_UNUSED = null;

    @Test
    void onMessage_shouldRouteTopics() {
        assertDoesNotThrow(() -> {
            consumer.onMessage(new ConsumerRecord<>(KafkaConstants.SUSPICIOUS_CREATE, PARTITION, OFFSET, KEY_UNUSED,
                    PAYLOAD_CREATE));
            consumer.onMessage(new ConsumerRecord<>(KafkaConstants.SUSPICIOUS_UPDATE, PARTITION, OFFSET, KEY_UNUSED,
                    PAYLOAD_UPDATE));
            consumer.onMessage(new ConsumerRecord<>(KafkaConstants.SUSPICIOUS_DELETE, PARTITION, OFFSET, KEY_UNUSED,
                    PAYLOAD_DELETE));
            consumer.onMessage(new ConsumerRecord<>(KafkaConstants.SUSPICIOUS_GET, PARTITION, OFFSET, KEY_UNUSED,
                    PAYLOAD_GET));
        });
    }

    @Test
    void onMessage_shouldGracefullyHandleUnknownTopic() {
        assertDoesNotThrow(() -> consumer
                .onMessage(new ConsumerRecord<>(TOPIC_UNKNOWN, PARTITION, OFFSET, KEY_UNUSED, PAYLOAD_UNKNOWN)));
    }
}
