package com.example.eindopdrachtbackend.dtos;

public class ReviewResponseDto {

    private Long id;
    private Integer rating;
    private String comment;
    private String reviewerUsername;
    private String reviewerAvatar;
    private Long gameId;
    private String gameTitle;
    private int upvotes;
    private int downvotes;
    private int totalScore;

    public ReviewResponseDto() {}

    public ReviewResponseDto(Long id, Integer rating, String comment, String reviewerUsername,
                            String reviewerAvatar, Long gameId, String gameTitle,
                            int upvotes, int downvotes) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.reviewerUsername = reviewerUsername;
        this.reviewerAvatar = reviewerAvatar;
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.totalScore = upvotes - downvotes;
    }

    public String getReviewerAvatar() {
        return reviewerAvatar;
    }

    public void setReviewerAvatar(String reviewerAvatar) {
        this.reviewerAvatar = reviewerAvatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewerUsername() {
        return reviewerUsername;
    }

    public void setReviewerUsername(String reviewerUsername) {
        this.reviewerUsername = reviewerUsername;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
