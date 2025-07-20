package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.UserProfileResponseDto;
import com.example.eindopdrachtbackend.models.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileResponseDto toResponseDto(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setId(profile.getId());
        dto.setUsername(profile.getUsername());
        dto.setAvatar(profile.getAvatar());
        dto.setBio(profile.getBio());
        dto.setPreferredGenres(profile.getPreferredGenres());

        return dto;
    }
}
