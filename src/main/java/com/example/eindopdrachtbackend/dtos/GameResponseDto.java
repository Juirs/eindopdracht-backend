package com.example.eindopdrachtbackend.dtos;

import com.example.eindopdrachtbackend.models.GameGenre;

import java.util.List;

public class GameResponseDto {

    private Long id;
    private String title;
    private String description;
    private GameGenre category;
    private String gameFilePath;
    private String imageUrl;
    private String trailerUrl;
    private List<String> screenshots;
    private String developerUsername;
    private int reviewCount;
    private Double averageRating;

    public GameResponseDto() {}

    public GameResponseDto(Long id, String title, String description, GameGenre category,
                          String gameFilePath, String imageUrl, String trailerUrl,
                          List<String> screenshots, String developerUsername) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.gameFilePath = gameFilePath;
        this.imageUrl = imageUrl;
        this.trailerUrl = trailerUrl;
        this.screenshots = screenshots;
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

    public String getGameFilePath() {
        return gameFilePath;
    }

    public void setGameFilePath(String gameFilePath) {
        this.gameFilePath = gameFilePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
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
