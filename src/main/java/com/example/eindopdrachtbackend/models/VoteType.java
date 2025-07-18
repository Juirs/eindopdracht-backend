package com.example.eindopdrachtbackend.models;

public enum VoteType {
    UPVOTE("Upvote"),
    DOWNVOTE("Downvote");

    private final String displayName;

    VoteType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
