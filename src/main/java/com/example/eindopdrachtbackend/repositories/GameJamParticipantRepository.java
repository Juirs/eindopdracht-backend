package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.GameJamParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameJamParticipantRepository extends JpaRepository<GameJamParticipant, Long> {

    // Check if user already joined a specific game jam
    Optional<GameJamParticipant> findByGameJamIdAndUserUsername(Long gameJamId, String username);
}
