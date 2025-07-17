package com.example.eindopdrachtbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String avatar;
    private String username;

    @ElementCollection(targetClass = GameGenre.class)
    @CollectionTable(name = "user_preferred_genres", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<GameGenre> preferredGenres;

    @Column(length = 500)
    private String bio;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<GameGenre> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(Set<GameGenre> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
