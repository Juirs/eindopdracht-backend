package com.example.eindopdrachtbackend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_jam_participants",
       uniqueConstraints = @UniqueConstraint(columnNames = {"game_jam_id", "user_id"}))
public class GameJamParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_jam_id", nullable = false)
    private GameJam gameJam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Game submission;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    public GameJamParticipant() {}

    public GameJamParticipant(GameJam gameJam, User user) {
        this.gameJam = gameJam;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameJam getGameJam() {
        return gameJam;
    }

    public void setGameJam(GameJam gameJam) {
        this.gameJam = gameJam;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Game getSubmission() {
        return submission;
    }

    public void setSubmission(Game submission) {
        this.submission = submission;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    // Override equals and hashCode for proper Set behavior
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameJamParticipant that = (GameJamParticipant) o;
        return gameJam != null && user != null &&
               gameJam.getId() != null && user.getUsername() != null &&
               gameJam.getId().equals(that.gameJam.getId()) &&
               user.getUsername().equals(that.user.getUsername());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
