package com.example.eindopdrachtbackend.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class GameJamRequestDto {

    @NotBlank(message = "Game jam name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Rules are required")
    @Size(max = 1000, message = "Rules cannot exceed 1000 characters")
    private String rules;

    @NotBlank(message = "Theme is required")
    @Size(max = 100, message = "Theme cannot exceed 100 characters")
    private String theme;

    private String gameJamImageUrl;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @Positive(message = "Max participants must be positive")
    private int maxParticipants = 100;

    public GameJamRequestDto() {}

    public GameJamRequestDto(String name, String description, String rules, String theme,
                            String gameJamImageUrl, LocalDateTime startDate, LocalDateTime endDate,
                            int maxParticipants) {
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.theme = theme;
        this.gameJamImageUrl = gameJamImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getGameJamImageUrl() {
        return gameJamImageUrl;
    }

    public void setGameJamImageUrl(String gameJamImageUrl) {
        this.gameJamImageUrl = gameJamImageUrl;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
