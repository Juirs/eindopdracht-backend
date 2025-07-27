package com.example.eindopdrachtbackend.dtos;

import com.example.eindopdrachtbackend.models.GameGenre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class GameRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Category is required")
    private GameGenre category;

    private String gameFilePath;

    private String imageUrl;

    private String trailerUrl;

    private List<String> screenshots;

    public GameRequestDto() {}

    public GameRequestDto(String title, String description, GameGenre category, String gameFilePath,
                         String imageUrl, String trailerUrl, List<String> screenshots) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.gameFilePath = gameFilePath;
        this.imageUrl = imageUrl;
        this.trailerUrl = trailerUrl;
        this.screenshots = screenshots;
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
}
