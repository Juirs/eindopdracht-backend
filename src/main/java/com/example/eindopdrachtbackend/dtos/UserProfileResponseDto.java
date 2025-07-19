package com.example.eindopdrachtbackend.dtos;

import com.example.eindopdrachtbackend.models.GameGenre;

import java.util.Set;

public class UserProfileResponseDto {

    private Long id;
    private String username;
    private String avatar;
    private String bio;
    private Set<GameGenre> preferredGenres;

    public UserProfileResponseDto() {}

    public UserProfileResponseDto(Long id, String username, String avatar, String bio, Set<GameGenre> preferredGenres) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.bio = bio;
        this.preferredGenres = preferredGenres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<GameGenre> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(Set<GameGenre> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }
}
