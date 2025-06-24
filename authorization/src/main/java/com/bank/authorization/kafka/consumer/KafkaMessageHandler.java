package com.bank.authorization.kafka.consumer;

import com.bank.authorization.validate.KafkaJwtValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageHandler {

    private final static int MAX_LIMIT = 3;
    private final KafkaJwtValidator kafkaJwtValidator;

    public <T> void processAdminAction(T request, String token, Consumer<T> action) {
        validateToken(token);
        log.debug("Обработка запроса: {} для AdminAction", request);
        try {
            action.accept(request);
        } catch (Exception e) {
            log.error("Обработка ошибки: {} для AdminAction", request, e);
            throw e;
        }
    }

    public <T, R> R processAdminActionWithResult(T request, String token, Function<T, R> action) {
        validateToken(token);
        log.debug("Обработка запроса: {} для AdminActionWithRes", request);
        try {
            return action.apply(request);
        } catch (Exception e) {
            log.error("Обработка ошибки: {} для AdminActionWithRes", request, e);
            throw e;
        }
    }

    private void validateToken(String token) {
        if (token != null) {
            kafkaJwtValidator.validateAdminAndSetContext(token);
        }
    }

    public static void logResponse(String entityName, Collection<?> items) {
        if (!items.isEmpty()) {
            log.info("{} items found. Examples: {}",
                    items.size(),
                    items.stream()
                            .limit(MAX_LIMIT)
                            .map(Object::toString)
                            .collect(Collectors.joining("; ")));
        } else {
            log.info("Empty response for {}", entityName);
        }
    }
}
