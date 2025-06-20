package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDeleteDto;
import com.bank.profile.Exceptions.EntityNotFoundException;
import com.bank.profile.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer, который отвечает за удаление профиля
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileDeleteConsumer {

    private final ProfileService profileService;

    @KafkaListener(topics = "${spring.kafka.topics.profile-delete}",
            groupId = "delete",
            errorHandler = "profileKafkaErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.profile.DTO.ProfileDeleteDto"
            })

    public void deleteProfile(ProfileDeleteDto profileDeleteDto) {
        try {
            if (profileDeleteDto == null) {
                throw new EntityNotFoundException("Not found ID");
            }
            profileService.deleteProfile(profileDeleteDto.getId());
        } catch (EntityNotFoundException e) {
            log.error("Profile deletion error: {}", e.getMessage());
        }
    }
}
