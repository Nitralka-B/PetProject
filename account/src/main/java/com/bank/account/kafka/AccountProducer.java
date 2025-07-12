package com.bank.account.kafka;

import com.bank.account.dto.AccountDto;
import com.bank.account.util.AccountTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka-продюсер для отправки событий, связанных с операциями над учетными записями.
 * <p>
 * Публикует сообщения в соответствующие Kafka-топики: создание, обновление, удаление и получение учетных записей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountProducer {
    private final KafkaTemplate<String, AccountDto> kafkaTemplate;

    public void send(AccountTopic topic, AccountDto message) {
        log.info(">> KAFKA | Message was send | ACCOUNT TOPIC | {}", topic.getTopic());
        kafkaTemplate.send(topic.getTopic(), message);
    }
}

