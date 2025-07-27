package com.example.eindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameGenre category;

    @Column(name = "game_file_path")
    private String gameFilePath;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @ElementCollection
    @CollectionTable(name = "game_screenshots", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "screenshot_url")
    @OrderColumn(name = "screenshot_order")
    private List<String> screenshots = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", nullable = false)
    private User developer;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GameReview> reviews = new ArrayList<>();

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

    public User getDeveloper() {
        return developer;
    }

    public void setDeveloper(User developer) {
        this.developer = developer;
    }

    public List<GameReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<GameReview> reviews) {
        this.reviews = reviews;
    }
}
