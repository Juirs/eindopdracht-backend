package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.UserProfileRequestDto;
import com.example.eindopdrachtbackend.dtos.UserProfileResponseDto;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.models.UserProfile;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.services.AvatarService;
import com.example.eindopdrachtbackend.services.UserProfileService;
import com.example.eindopdrachtbackend.utils.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/users/{username}/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;
    private final AvatarService avatarService;

    public UserProfileController(UserProfileService userProfileService, UserRepository userRepository, AvatarService avatarService) {
        this.userProfileService = userProfileService;
        this.userRepository = userRepository;
        this.avatarService = avatarService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDto> getUserProfile(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails currentUser) {

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, username)) {
            throw new AccessDeniedException("You can only access your own profile");
        }

        UserProfileResponseDto profile = userProfileService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            @PathVariable String username,
            @Valid @RequestBody UserProfileRequestDto requestDto,
            @AuthenticationPrincipal UserDetails currentUser) {

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, username)) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        UserProfileResponseDto updatedProfile = userProfileService.updateUserProfile(username, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/avatar")
    public ResponseEntity<UserProfileResponseDto> uploadAvatar(
            @PathVariable String username,
            @RequestParam("avatar") MultipartFile avatarFile,
            @AuthenticationPrincipal UserDetails currentUser) throws IOException {

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, username)) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        UserProfileResponseDto updatedProfile = userProfileService.uploadAvatar(username, avatarFile);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/avatar")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable String username) throws IOException {

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserProfile profile = user.getUserProfile();
        if (profile == null || profile.getAvatar() == null || profile.getAvatar().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Path path = avatarService.getAvatarPath(profile.getAvatar());

        return getResourceResponseEntity(path);
    }

    @NotNull
    static ResponseEntity<Resource> getResourceResponseEntity(Path path) throws IOException {
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }
}
