package com.example.eindopdrachtbackend.services;

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


    public List<UserResponseDto> getUsers() {
        List<UserResponseDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserResponseDto getUser(String username) {
        UserResponseDto dto = new UserResponseDto();
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            dto = fromUser(user.get());
        }else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserResponseDto userResponseDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userResponseDto.setApikey(randomString);
        User newUser = userRepository.save(toUser(userResponseDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserResponseDto newUser) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.setPassword(newUser.getPassword());
        userRepository.save(user);
    }

    public Set<Role> getRoles(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserResponseDto userResponseDto = fromUser(user);
        return userResponseDto.getRoles();
    }

    public void addRole(String username, String role) {

        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();

        // Get existing roles or create new set if null
        Set<Role> userRoles = user.getRoles();
        if (userRoles == null) {
            userRoles = new HashSet<>();
        }

        // Add the new role to the set
        userRoles.add(new Role(username, role));
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    public void removeRole(String username, String role) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Role roleToRemove = user.getRoles().stream().filter((a) -> a.getRole().equalsIgnoreCase(role)).findAny().get();
        user.removeRole(roleToRemove);
        userRepository.save(user);
    }

    public static UserResponseDto fromUser(User user){

        var dto = new UserResponseDto();

        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEnabled(user.getEnabled());
        dto.setApikey(user.getApikey());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());

        return dto;
    }

    public User toUser(UserResponseDto userResponseDto) {

        var user = new User();

        user.setUsername(userResponseDto.getUsername());
        user.setPassword(encoder.encode(userResponseDto.getPassword()));
        user.setEnabled(userResponseDto.getEnabled());
        user.setApikey(userResponseDto.getApikey());
        user.setEmail(userResponseDto.getEmail());

        return user;
    }

}
