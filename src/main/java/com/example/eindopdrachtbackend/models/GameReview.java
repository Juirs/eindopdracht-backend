package com.example.eindopdrachtbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_reviews")
public class GameReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating; // 1-5 stars

    @Column(length = 1000)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reviewer;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReviewVote> votes = new ArrayList<>();

    public GameReview() {}

    public GameReview(Integer rating, String comment, Game game, User reviewer) {
        this.rating = rating;
        this.comment = comment;
        this.game = game;
        this.reviewer = reviewer;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public List<ReviewVote> getVotes() {
        return votes;
    }

    public void setVotes(List<ReviewVote> votes) {
        this.votes = votes;
    }

    public int getUpvoteCount() {
        return votes != null ? (int) votes.stream()
                .filter(vote -> vote.getVoteType() == VoteType.UPVOTE)
                .count() : 0;
    }

    public int getDownvoteCount() {
        return votes != null ? (int) votes.stream()
                .filter(vote -> vote.getVoteType() == VoteType.DOWNVOTE)
                .count() : 0;
    }

    public int getTotalScore() {
        return getUpvoteCount() - getDownvoteCount();
    }
}
