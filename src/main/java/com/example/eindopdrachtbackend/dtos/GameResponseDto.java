package com.example.eindopdrachtbackend.dtos;

import com.example.eindopdrachtbackend.models.GameGenre;

public class GameResponseDto {

    private Long id;
    private String title;
    private String description;
    private GameGenre category;
    private String filePath;
    private String developerUsername;
    private int reviewCount;
    private Double averageRating;

    public GameResponseDto() {}

    public GameResponseDto(Long id, String title, String description, GameGenre category,
                          String filePath, String developerUsername) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.filePath = filePath;
        this.developerUsername = developerUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GameGenre getCategory() {
        return category;
    }

    public void setCategory(GameGenre category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDeveloperUsername() {
        return developerUsername;
    }

    public void setDeveloperUsername(String developerUsername) {
        this.developerUsername = developerUsername;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
