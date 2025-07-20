package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.UserProfileRequestDto;
import com.example.eindopdrachtbackend.dtos.UserProfileResponseDto;
import com.example.eindopdrachtbackend.mappers.UserProfileMapper;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.models.UserProfile;
import com.example.eindopdrachtbackend.repositories.UserProfileRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AvatarService avatarService;
    private final UserProfileMapper userProfileMapper;

    public UserProfileService(UserProfileRepository userProfileRepository,
                            UserRepository userRepository,
                            AvatarService avatarService,
                            UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.avatarService = avatarService;
        this.userProfileMapper = userProfileMapper;
    }

    public UserProfileResponseDto getUserProfile(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserProfile profile = user.getUserProfile();
        return userProfileMapper.toResponseDto(profile);
    }

    public UserProfileResponseDto updateUserProfile(String username, UserProfileRequestDto requestDto) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserProfile profile = user.getUserProfile();

        updateProfileFromDto(profile, requestDto);
        UserProfile savedProfile = userProfileRepository.save(profile);

        return userProfileMapper.toResponseDto(savedProfile);
    }

    public UserProfileResponseDto uploadAvatar(String username, MultipartFile avatarFile) throws IOException {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserProfile profile = user.getUserProfile();

        if (profile.getAvatar() != null && !profile.getAvatar().isEmpty()) {
            avatarService.deleteAvatar(profile.getAvatar());
        }

        String avatarPath = avatarService.uploadAvatar(avatarFile, username);
        profile.setAvatar(avatarPath);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return userProfileMapper.toResponseDto(savedProfile);
    }

    private void updateProfileFromDto(UserProfile profile, UserProfileRequestDto requestDto) {
        if (requestDto.getAvatar() != null) {
            profile.setAvatar(requestDto.getAvatar());
        }
        if (requestDto.getBio() != null) {
            profile.setBio(requestDto.getBio());
        }
        if (requestDto.getPreferredGenres() != null) {
            profile.setPreferredGenres(requestDto.getPreferredGenres());
        }
    }
}
