package com.example.eindopdrachtbackend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendships",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
public class FriendsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", referencedColumnName = "username")
    @NotNull(message = "Friend is required")
    private User friend;

    @Column(name = "status", nullable = false)
    @NotBlank(message = "Status is required")
    private String status;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created date is required")
    private LocalDateTime createdAt;
    
    public FriendsList() {}
    
    public FriendsList(User user, User friend, String status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getFriend() {
        return friend;
    }
    
    public void setFriend(User friend) {
        this.friend = friend;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
