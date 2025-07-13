package com.bank.account.kafka;

import com.bank.account.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka-консьюмер для получения событий, связанных с учетными записями.
 * <p>
 * Подписан на топики: {@code account.create}, {@code account.update},
 * {@code account.delete}, {@code account.get}. Обрабатывает входящие сообщения,
 * выводя их в консоль.
 */
@Service
@Slf4j
public class AccountConsumer {
    /**
     * Обработчик Kafka-сообщений из топиков учетных записей.
     * <p>
     * Метод автоматически вызывается при получении сообщений
     * из указанных Kafka-топиков.
     *
     * @param message текст сообщения, полученного из Kafka
     */
    @KafkaListener(topics = {"account.create", "account.update", "account.delete",
            "account.get"}, groupId = "account-group")
    public void consume(AccountDto message) {
        log.info(">> KAFKA | Received message: {}", message);
    }
}
