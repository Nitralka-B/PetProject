package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.DTO.ProfileIdDto;
import com.bank.profile.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer, который отвечает за отправку Json с полученным профилем в топик profile.getRequest
 */
@Service
@RequiredArgsConstructor
public class ProfileGetProducer {

    private final KafkaTemplate<String, ProfileDto> kafkaTemplate;
    private final ProfileService profileService;

    public void getProfile(ProfileIdDto profileIdDto) {
        kafkaTemplate.send("${kafka.spring.topics.profile-get}", profileService.getProfile(profileIdDto.getId()));
    }
}
