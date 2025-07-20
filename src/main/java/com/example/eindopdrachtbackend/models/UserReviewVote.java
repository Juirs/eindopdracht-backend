package com.example.eindopdrachtbackend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_review_votes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "review_id"}))
public class UserReviewVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String username;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    public UserReviewVote() {}

    public UserReviewVote(String username, Long reviewId, VoteType voteType) {
        this.username = username;
        this.reviewId = reviewId;
        this.voteType = voteType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }
}
