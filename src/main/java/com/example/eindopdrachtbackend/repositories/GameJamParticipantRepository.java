package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.GameJamParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameJamParticipantRepository extends JpaRepository<GameJamParticipant, Long> {

    // Check if user already joined a specific game jam
    Optional<GameJamParticipant> findByGameJamIdAndUserUsername(Long gameJamId, String username);

    @Modifying
    @Query("UPDATE GameJamParticipant SET submission = null, submissionDate = null WHERE submission.id = :gameId")
    void removeGameFromSubmissions(@Param("gameId") Long gameId);
}
