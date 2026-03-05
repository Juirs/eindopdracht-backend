package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.FriendsListResponseDto;
import com.example.eindopdrachtbackend.models.FriendsList;
import org.springframework.stereotype.Component;

@Component
public class FriendsListMapper {

    public FriendsListResponseDto toResponseDto(FriendsList friendsList, String currentUsername) {
        if (friendsList == null) {
            return null;
        }

        FriendsListResponseDto dto = new FriendsListResponseDto();
        dto.setId(friendsList.getId());
        
        // Return the username of the OTHER person in the friendship
        String otherUsername = friendsList.getUser().getUsername().equals(currentUsername)
                ? friendsList.getFriend().getUsername()
                : friendsList.getUser().getUsername();
        
        dto.setUsername(otherUsername);
        dto.setStatus(friendsList.getStatus());
        dto.setCreatedAt(friendsList.getCreatedAt());

        return dto;
    }

    public FriendsListResponseDto toResponseDto(FriendsList friendsList) {
        if (friendsList == null) {
            return null;
        }

        FriendsListResponseDto dto = new FriendsListResponseDto();
        dto.setId(friendsList.getId());
        dto.setUsername(friendsList.getFriend().getUsername());
        dto.setStatus(friendsList.getStatus());
        dto.setCreatedAt(friendsList.getCreatedAt());

        return dto;
    }
}
