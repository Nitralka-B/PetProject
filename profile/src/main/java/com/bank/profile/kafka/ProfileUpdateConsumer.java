package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer, который отвечает за обновление профиля
 */
@Service
@RequiredArgsConstructor
public class ProfileUpdateConsumer {

    private final ProfileService profileService;

    @KafkaListener(topics = "${spring.kafka.topics.profile-update}",
            groupId = "profileUpdate",
            errorHandler = "profileKafkaErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.profile.DTO.ProfileDto"
            })

    public void updateProfile(ProfileDto profileDto) {
        profileService.updateProfile(profileDto);
    }
}
