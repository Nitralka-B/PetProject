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
public class ProfileUpdateConsumerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileUpdateConsumer profileUpdateConsumer;

    @Test
    void updateProfile_ShouldCallServiceWithValidDto() {
        ProfileDto profileDto = TestUtils.getProfileDtoForUpdate();

        profileUpdateConsumer.updateProfile(profileDto);

        verify(profileService, times(1)).updateProfile(profileDto);
    }
}
