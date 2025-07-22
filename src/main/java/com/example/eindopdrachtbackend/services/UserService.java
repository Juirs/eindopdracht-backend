package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.UserRequestDto;
import com.example.eindopdrachtbackend.dtos.UserResponseDto;
import com.example.eindopdrachtbackend.mappers.UserMapper;
import com.example.eindopdrachtbackend.models.Role;
import com.example.eindopdrachtbackend.models.RoleKey;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.models.UserProfile;
import com.example.eindopdrachtbackend.repositories.RoleRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.utils.RandomStringGenerator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    // READ OPERATIONS
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return userMapper.toResponseDto(user);
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

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(newUser);
        userProfile.setBio(null);
        userProfile.setAvatar(null);
        userProfile.setPreferredGenres(new HashSet<>());

        newUser.setUserProfile(userProfile);

        User savedUser = userRepository.save(newUser);
        addRole(savedUser.getUsername(), "USER");

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

    public void changePassword(String username, String newPassword, UserDetails currentUser, EmailService emailService) {
        if (!currentUser.getUsername().equals(username)) {
            throw new AccessDeniedException("You can only change your own password");
        }

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        String subject = "Password Changed - IndieVerse";
        String body = String.format(
                """
                        Hello %s,
                        
                        Your password has been successfully changed.
                        
                        If you did not make this change, please contact support immediately.
                        
                        Best regards,
                        IndieVerse Team""",
            user.getUsername()
        );

        emailService.sendEmail(user.getEmail(), subject, body);
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

        RoleKey roleKey = new RoleKey(username, authority);
        Role role = roleRepository.findById(roleKey)
                .orElseGet(() -> {
                    Role newRole = new Role(username, authority);
                    return roleRepository.save(newRole);
                });

        user.addRole(role);
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
}
