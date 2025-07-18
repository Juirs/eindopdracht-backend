package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.UserRequestDto;
import com.example.eindopdrachtbackend.dtos.UserResponseDto;
import com.example.eindopdrachtbackend.models.Role;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.utils.RandomStringGenerator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    // READ OPERATIONS
    public List<UserResponseDto> getUsers() {
        List<UserResponseDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserResponseDto getUser(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            return fromUser(user.get());
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    // CREATE OPERATION
    public String createUser(UserRequestDto userRequestDto) {
        if (userExists(userRequestDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + userRequestDto.getUsername());
        }

        User newUser = new User();
        newUser.setUsername(userRequestDto.getUsername());
        newUser.setPassword(encoder.encode(userRequestDto.getPassword()));
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setEnabled(true);
        newUser.setApikey(RandomStringGenerator.generateAlphaNumeric(20));

        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(new Role(userRequestDto.getUsername(), "USER"));
        newUser.setRoles(defaultRoles);

        User savedUser = userRepository.save(newUser);
        return savedUser.getUsername();
    }

    // UPDATE OPERATIONS
    public void updateUser(String username, UserRequestDto updateRequest) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userRepository.findById(username).get();
        user.setEmail(updateRequest.getEmail());
        userRepository.save(user);
    }

    public void updatePassword(String username, String newPassword) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userRepository.findById(username).get();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public void enableUser(String username, boolean enabled) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        User user = userRepository.findById(username).get();
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    // DELETE OPERATION
    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        userRepository.deleteById(username);
    }

    // ROLE MANAGEMENT
    public Set<Role> getRoles(String username) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        User user = userRepository.findById(username).get();
        return user.getRoles();
    }

    public void addRole(String username, String authority) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        User user = userRepository.findById(username).get();
        user.addRole(new Role(username, authority));
        userRepository.save(user);
    }

    public void removeRole(String username, String authority) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        User user = userRepository.findById(username).get();
        Role authorityToRemove = user.getRoles().stream()
                .filter((a) -> a.getRole().equalsIgnoreCase(authority))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Role not found: " + authority));
        user.removeRole(authorityToRemove);
        userRepository.save(user);
    }

    public User getUserForAuthentication(String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user.get();
    }

    public static UserResponseDto fromUser(User user) {
        var dto = new UserResponseDto();

        dto.setUsername(user.getUsername());
        dto.setEnabled(user.getEnabled());
        dto.setApikey(user.getApikey());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());

        if (user.getUserProfile() != null) {
            dto.setAvatar(user.getUserProfile().getAvatar());
            dto.setBio(user.getUserProfile().getBio());
            dto.setPreferredGenres(user.getUserProfile().getPreferredGenres());
        }

        dto.setGamesCreated(user.getGames() != null ? user.getGames().size() : 0);
        dto.setReviewsWritten(user.getReviews() != null ? user.getReviews().size() : 0);

        return dto;
    }
}
