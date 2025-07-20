package com.example.eindopdrachtbackend.dtos;

import java.time.LocalDateTime;

public class GameJamResponseDto {

    private Long id;
    private String name;
    private String description;
    private String rules;
    private String theme;
    private String gameJamImageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private int maxParticipants;
    private int currentParticipants;
    private Boolean isActive;
    private boolean isCurrentlyActive;
    private boolean canAcceptParticipants;

    public GameJamResponseDto() {}

    public GameJamResponseDto(Long id, String name, String description, String rules, String theme,
                             String gameJamImageUrl, LocalDateTime startDate, LocalDateTime endDate,
                             LocalDateTime createdAt, int maxParticipants, int currentParticipants,
                             Boolean isActive, boolean isCurrentlyActive, boolean canAcceptParticipants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.theme = theme;
        this.gameJamImageUrl = gameJamImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.isActive = isActive;
        this.isCurrentlyActive = isCurrentlyActive;
        this.canAcceptParticipants = canAcceptParticipants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isCurrentlyActive() {
        return isCurrentlyActive;
    }

    public void setCurrentlyActive(boolean currentlyActive) {
        isCurrentlyActive = currentlyActive;
    }

    public boolean isCanAcceptParticipants() {
        return canAcceptParticipants;
    }

    public void setCanAcceptParticipants(boolean canAcceptParticipants) {
        this.canAcceptParticipants = canAcceptParticipants;
    }
}
