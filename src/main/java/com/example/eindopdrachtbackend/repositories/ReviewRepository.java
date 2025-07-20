package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.GameReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<GameReview, Long> {
}
