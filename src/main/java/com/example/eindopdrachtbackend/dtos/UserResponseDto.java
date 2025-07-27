package com.example.eindopdrachtbackend.dtos;

import java.util.Set;

public class UserResponseDto {

    private String username;
    private String email;
    private Boolean enabled;
    private String apikey;
    private Set<String> roles;
    private int gamesCreated;
    private int reviewsWritten;

    public UserResponseDto() {}

    public UserResponseDto(String username, String email, Boolean enabled, String apikey,
                          Set<String> roles, int gamesCreated, int reviewsWritten) {
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.apikey = apikey;
        this.roles = roles;
        this.gamesCreated = gamesCreated;
        this.reviewsWritten = reviewsWritten;
    }

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

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
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
