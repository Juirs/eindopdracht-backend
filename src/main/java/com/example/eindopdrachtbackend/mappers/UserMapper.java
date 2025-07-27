package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.UserResponseDto;
import com.example.eindopdrachtbackend.models.Role;
import com.example.eindopdrachtbackend.models.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();

        dto.setUsername(user.getUsername());
        dto.setEnabled(user.getEnabled());
        dto.setApikey(user.getApikey());
        dto.setEmail(user.getEmail());

        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getRole)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }

        dto.setGamesCreated(user.getGames() != null ? user.getGames().size() : 0);
        dto.setReviewsWritten(user.getReviews() != null ? user.getReviews().size() : 0);

        return dto;
    }
}
