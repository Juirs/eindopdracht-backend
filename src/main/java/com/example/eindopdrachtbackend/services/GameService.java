package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.GameRequestDto;
import com.example.eindopdrachtbackend.dtos.GameResponseDto;
import com.example.eindopdrachtbackend.exceptions.NameTooLongException;
import com.example.eindopdrachtbackend.exceptions.RecordNotFoundException;
import com.example.eindopdrachtbackend.models.Game;
import com.example.eindopdrachtbackend.models.GameGenre;
import com.example.eindopdrachtbackend.models.GameReview;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.GameRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.utils.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public List<GameResponseDto> getAllGamesOrByCategory(GameGenre category) {
        List<Game> games = category != null
                ? gameRepository.findByCategory(category)
                : gameRepository.findAll();

        return games.stream()
                .map(GameService::fromGame)
                .toList();
    }

    public GameResponseDto getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + id + " not found."));
        return fromGame(game);
    }

    public GameResponseDto createGame(GameRequestDto gameRequestDto, UserDetails currentUser) {
        if (gameRequestDto.getTitle().length() > 100) {
            throw new NameTooLongException("Game title cannot be more than 100 characters.");
        }

        String developerUsername = currentUser.getUsername();
        User developer = userRepository.findById(developerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Developer not found: " + developerUsername));

        Game game = new Game();
        game.setTitle(gameRequestDto.getTitle());
        game.setCategory(gameRequestDto.getCategory());
        game.setDescription(gameRequestDto.getDescription());
        game.setFilePath(gameRequestDto.getFilePath());
        game.setDeveloper(developer);

        Game savedGame = gameRepository.save(game);

        return fromGame(savedGame);
    }

    public GameResponseDto updateGame(Long id, GameRequestDto gameRequestDto, UserDetails currentUser) {
        if (gameRequestDto.getTitle().length() > 100) {
            throw new NameTooLongException("Game title cannot be more than 100 characters.");
        }

        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + id + " not found."));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, existingGame.getDeveloper().getUsername())) {
            throw new AccessDeniedException("You can only update your own games");
        }

        existingGame.setTitle(gameRequestDto.getTitle());
        existingGame.setCategory(gameRequestDto.getCategory());
        existingGame.setDescription(gameRequestDto.getDescription());
        existingGame.setFilePath(gameRequestDto.getFilePath());

        gameRepository.save(existingGame);

        return fromGame(existingGame);
    }

    public void deleteGame(Long id, UserDetails currentUser) {
        if (id == null) {
            throw new RecordNotFoundException("Game ID cannot be null.");
        }

        Game gameToDelete = gameRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + id + " not found."));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, gameToDelete.getDeveloper().getUsername())) {
            throw new AccessDeniedException("You can only delete your own games");
        }

        gameRepository.deleteById(id);
    }

    // Entity to DTO mapping
    public static GameResponseDto fromGame(Game game) {
        GameResponseDto dto = new GameResponseDto();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setDescription(game.getDescription());
        dto.setCategory(game.getCategory());
        dto.setFilePath(game.getFilePath());
        dto.setDeveloperUsername(game.getDeveloper().getUsername());

        addReviewStatistics(dto, game);

        return dto;
    }

    private static void addReviewStatistics(GameResponseDto dto, Game game) {
        if (game.getReviews() == null || game.getReviews().isEmpty()) {
            dto.setReviewCount(0);
            dto.setAverageRating(null);
            return;
        }

        dto.setReviewCount(game.getReviews().size());

        double averageRating = calculateAverageRating(game);
        dto.setAverageRating(averageRating);
    }

    public static double calculateAverageRating(Game game) {
        if (game.getReviews() == null || game.getReviews().isEmpty()) {
            return 0.0;
        }

        return game.getReviews().stream()
                .mapToInt(GameReview::getRating)
                .average()
                .orElse(0.0);
    }

    public static int getReviewCount(Game game) {
        return game.getReviews() != null ? game.getReviews().size() : 0;
    }
}
