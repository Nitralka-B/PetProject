package com.bank.profile.Mappers;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для профиля
 */
@Mapper(componentModel = "spring", uses = {PassportMapper.class, RegistrationMapper.class})
public interface ProfileMapper {

    @Mapping(source = "passport", target = "passportDto")
    ProfileDto profileToProfileDto(Profile profile);

    @Mapping(source = "passportDto", target = "passport")
    Profile profileDtoToProfile(ProfileDto profileDto);
}
