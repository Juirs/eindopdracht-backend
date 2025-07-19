package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
