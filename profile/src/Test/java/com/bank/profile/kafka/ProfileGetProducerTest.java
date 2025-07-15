package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.DTO.ProfileIdDto;
import com.bank.profile.Services.ProfileService;
import com.bank.profile.Utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileGetProducerTest {

    @Mock
    private KafkaTemplate<String, ProfileDto> kafkaTemplate;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileGetProducer profileGetProducer;

    @Test
    void getProfile_ShouldSendCorrectProfileToKafka() {
        // Given
        ProfileIdDto profileIdDto = new ProfileIdDto();
        profileIdDto.setId(1L);
        ProfileDto expectedProfile = TestUtils.getProfileDtoForUpdate();

        when(profileService.getProfile(profileIdDto.getId())).thenReturn(expectedProfile);

        // When
        profileGetProducer.getProfile(profileIdDto);

        // Then
        verify(profileService, times(1)).getProfile(profileIdDto.getId());

        // Используем ArgumentCaptor для проверки аргументов
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProfileDto> profileCaptor = ArgumentCaptor.forClass(ProfileDto.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), profileCaptor.capture());

        assertEquals(expectedProfile, profileCaptor.getValue());
        assertEquals("${kafka.spring.topics.profile-get}", topicCaptor.getValue());
    }
}
