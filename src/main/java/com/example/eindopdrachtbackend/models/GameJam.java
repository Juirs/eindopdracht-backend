package com.example.eindopdrachtbackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "game_jams")
public class GameJam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Rules are required")
    @Size(max = 1000, message = "Rules cannot exceed 1000 characters")
    @Column(length = 1000)
    private String rules;

    @NotBlank(message = "Theme is required")
    @Size(max = 100, message = "Theme cannot exceed 100 characters")
    private String theme;

    private String gameJamImageUrl;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Min(value = 1, message = "Max participants must be at least 1")
    private int maxParticipants = 100;

    @Column(name = "current_participants")
    private int currentParticipants = 0;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "gameJam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GameJamParticipant> participants = new HashSet<>();

    public GameJam() {}

    public GameJam(String name, String description, String rules, String theme, String gameJamImageUrl, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.theme = theme;
        this.gameJamImageUrl = gameJamImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Business logic methods, will be moved to a service layer once I have it
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && now.isAfter(startDate) && now.isBefore(endDate);
    }

    public boolean canAcceptParticipants() {
        return isCurrentlyActive() && getCurrentParticipantCount() < maxParticipants;
    }

    public int getCurrentParticipantCount() {
        return participants != null ? participants.size() : 0;
    }

    public boolean addParticipant(GameJamParticipant participant) {
        if (!canAcceptParticipants()) {
            return false;
        }
        boolean added = participants.add(participant);
        if (added) {
            this.currentParticipants = getCurrentParticipantCount();
        }
        return added;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<GameJamParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<GameJamParticipant> participants) {
        this.participants = participants;
        this.currentParticipants = getCurrentParticipantCount();
    }
}
