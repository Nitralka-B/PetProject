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

    private final KafkaJwtValidator kafkaJwtValidator;

    public <T> void processAdminAction(T request, String token, Consumer<T> action) {
        validateToken(token);
        log.debug("Processing request: {}", request);
        try {
            action.accept(request);
        } catch (Exception e) {
            log.error("Processing error: {}", request, e);
            throw e;
        }
    }

    public <T, R> R processAdminActionWithResult(T request, String token, Function<T, R> action) {
        validateToken(token);
        log.debug("Processing request: {}", request);
        try {
            return action.apply(request);
        } catch (Exception e) {
            log.error("Processing error: {}", request, e);
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
                            .limit(3)
                            .map(Object::toString)
                            .collect(Collectors.joining("; ")));
        } else {
            log.info("Empty response for {}", entityName);
        }
    }
}
