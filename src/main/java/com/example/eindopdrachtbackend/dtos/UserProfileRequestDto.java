package com.example.eindopdrachtbackend.dtos;

import com.example.eindopdrachtbackend.models.GameGenre;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserProfileRequestDto {

    private String avatar;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    private Set<GameGenre> preferredGenres;

    public UserProfileRequestDto() {}

    public UserProfileRequestDto(String avatar, String bio, Set<GameGenre> preferredGenres) {
        this.avatar = avatar;
        this.bio = bio;
        this.preferredGenres = preferredGenres;
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
