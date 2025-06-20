package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer, отвечающий за создание профиля
 */
@Service
@RequiredArgsConstructor
public class ProfileCreateConsumer {

    private final ProfileService profileService;

    @KafkaListener(topics = "${spring.kafka.topics.profile-create}",
            groupId = "Create",
            errorHandler = "profileKafkaErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.profile.DTO.ProfileDto"
            })

    public void createProfile(ProfileDto profileDto) {
        profileService.createProfile(profileDto);
    }
}
