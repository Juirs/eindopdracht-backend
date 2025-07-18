package com.example.eindopdrachtbackend.models;

public enum GameGenre {
    ACTION("Action"),
    RPG("Role-Playing"),
    STRATEGY("Strategy"),
    PUZZLE("Puzzle"),
    ADVENTURE("Adventure"),
    SIMULATION("Simulation"),
    SPORTS("Sports"),
    RACING("Racing"),
    SHOOTER("Shooter"),
    PLATFORMER("Platformer"),
    HORROR("Horror"),
    INDIE("Indie");

    private final String displayName;

    GameGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
