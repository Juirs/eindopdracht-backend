package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.ChangePasswordRequestDto;
import com.example.eindopdrachtbackend.dtos.RoleRequestDto;
import com.example.eindopdrachtbackend.dtos.UserRequestDto;
import com.example.eindopdrachtbackend.dtos.UserResponseDto;
import com.example.eindopdrachtbackend.exceptions.BadRequestException;
import com.example.eindopdrachtbackend.services.EmailService;
import com.example.eindopdrachtbackend.services.UserService;
import com.example.eindopdrachtbackend.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> userDtos = userService.getUsers();
        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("username") String username,
                                          @AuthenticationPrincipal UserDetails currentUser) {

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, username)) {
            throw new AccessDeniedException("You can only access your own profile");
        }

        UserResponseDto user = userService.getUser(username);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto dto,
                                                        @RequestParam(value = "isDeveloper", defaultValue = "false") boolean isDeveloper) {
        String newUsername = userService.createUser(dto, emailService);

        if (isDeveloper) {
            userService.addRole(newUsername, "DEVELOPER");
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/../{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable("username") String username,
                                          @Valid @RequestBody UserRequestDto dto,
                                          @AuthenticationPrincipal UserDetails currentUser) {

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, username)) {
            throw new AccessDeniedException("You can only update your own profile");
        }

        userService.updateUser(username, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getRoles(username));
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Void> addUserAuthority(@PathVariable("username") String username,
                                                 @RequestBody RoleRequestDto requestDto) {
        try {
            String authorityName = requestDto.getRole();
            userService.addRole(username, authorityName);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException("Failed to add authority: " + ex.getMessage());
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Void> deleteUserAuthority(@PathVariable("username") String username,
                                                    @PathVariable("authority") String authority) {
        userService.removeRole(username, authority);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{username}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable("username") String username,
                                                 @Valid @RequestBody ChangePasswordRequestDto requestDto) {
        userService.changePasswordWithoutAuth(username, requestDto.getNewPassword(), emailService);
        return ResponseEntity.ok("Password changed successfully. A confirmation email has been sent.");
    }
}
