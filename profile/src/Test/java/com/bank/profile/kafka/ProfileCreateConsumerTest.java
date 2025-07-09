package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Services.ProfileService;
import com.bank.profile.Utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProfileCreateConsumerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileCreateConsumer profileCreateConsumer;

    @Test
    void createProfile_ShouldCallServiceWithCorrectDto() {
        ProfileDto profileDto = TestUtils.getProfileForCreate();

        profileCreateConsumer.createProfile(profileDto);

        verify(profileService, times(1)).createProfile(profileDto);
    }
}
