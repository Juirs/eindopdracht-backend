package com.example.eindopdrachtbackend.dtos;

import com.example.eindopdrachtbackend.models.GameGenre;
import com.example.eindopdrachtbackend.models.Role;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Set;

public class UserResponseDto {

    private String username;
    private String email;
    private Boolean enabled;
    private String password;
    private String apikey;
    private String avatar;
    private String bio;
    private Set<GameGenre> preferredGenres;

    @JsonSerialize
    private Set<Role> roles;

    private int gamesCreated;
    private int reviewsWritten;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getGamesCreated() {
        return gamesCreated;
    }

    public void setGamesCreated(int gamesCreated) {
        this.gamesCreated = gamesCreated;
    }

    public int getReviewsWritten() {
        return reviewsWritten;
    }

    public void setReviewsWritten(int reviewsWritten) {
        this.reviewsWritten = reviewsWritten;
    }
}