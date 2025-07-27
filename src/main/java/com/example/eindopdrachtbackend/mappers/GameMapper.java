package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.GameResponseDto;
import com.example.eindopdrachtbackend.models.Game;
import com.example.eindopdrachtbackend.models.GameReview;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameResponseDto toResponseDto(Game game) {
        if (game == null) {
            return null;
        }

        GameResponseDto dto = new GameResponseDto();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setDescription(game.getDescription());
        dto.setCategory(game.getCategory());
        dto.setGameFilePath(game.getGameFilePath());
        dto.setImageUrl(game.getImageUrl());
        dto.setTrailerUrl(game.getTrailerUrl());
        dto.setScreenshots(game.getScreenshots()); // Now properly maps List<String>
        dto.setDeveloperUsername(game.getDeveloper().getUsername());

        addReviewStatistics(dto, game);

        return dto;
    }

    private void addReviewStatistics(GameResponseDto dto, Game game) {
        if (game.getReviews() == null || game.getReviews().isEmpty()) {
            dto.setReviewCount(0);
            dto.setAverageRating(null);
            return;
        }

        dto.setReviewCount(game.getReviews().size());

        double averageRating = game.getReviews().stream()
                .mapToInt(GameReview::getRating)
                .average()
                .orElse(0.0);
        dto.setAverageRating(averageRating);
    }
}
