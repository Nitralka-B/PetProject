package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer, который отвечает за отправку Json с полученным профилем в топик profile.getRequest
 */
@Service
@RequiredArgsConstructor
public class ProfileGetConsumer {

    private final ProfileGetProducer profileGetProducer;

    @KafkaListener(topics = "${spring.kafka.topics.profile-requestget}",
            groupId = "getProfile",
            errorHandler = "profileKafkaErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.profile.DTO.ProfileIdDto"
            })

    public void getProfile(ProfileIdDto profileIdDto) {
        profileGetProducer.getProfile(profileIdDto);
    }
}
