package com.example.eindopdrachtbackend.dtos;

import jakarta.validation.constraints.NotBlank;

public class FriendsListRequestDto {
    @NotBlank(message = "Friend username is required")
    private String friendUsername;
    
    public String getFriendUsername() {
        return friendUsername;
    }
    
    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }
}
