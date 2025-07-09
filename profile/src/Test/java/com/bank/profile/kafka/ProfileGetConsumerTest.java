package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileIdDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProfileGetConsumerTest {

    @Mock
    private ProfileGetProducer profileGetProducer;

    @InjectMocks
    private ProfileGetConsumer profileGetConsumer;

    @Test
    void getProfile_ShouldCallProducerWithCorrectIdDto() {
        ProfileIdDto profileIdDto = new ProfileIdDto();
        profileIdDto.setId(1L);

        profileGetConsumer.getProfile(profileIdDto);

        verify(profileGetProducer, times(1)).getProfile(profileIdDto);
    }
}
