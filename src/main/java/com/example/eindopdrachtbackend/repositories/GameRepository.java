package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.Game;
import com.example.eindopdrachtbackend.models.GameGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByCategory(GameGenre category);
}
