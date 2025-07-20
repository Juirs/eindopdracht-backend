package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.GameRequestDto;
import com.example.eindopdrachtbackend.dtos.GameResponseDto;
import com.example.eindopdrachtbackend.exceptions.NameTooLongException;
import com.example.eindopdrachtbackend.exceptions.RecordNotFoundException;
import com.example.eindopdrachtbackend.mappers.GameMapper;
import com.example.eindopdrachtbackend.models.Game;
import com.example.eindopdrachtbackend.models.GameGenre;
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
    private final GameMapper gameMapper;

    public GameService(GameRepository gameRepository, UserRepository userRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapper = gameMapper;
    }

    public List<GameResponseDto> getAllGamesOrByCategory(GameGenre category) {
        List<Game> games = category != null
                ? gameRepository.findByCategory(category)
                : gameRepository.findAll();

        return games.stream()
                .map(gameMapper::toResponseDto)
                .toList();
    }

    public GameResponseDto getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + id + " not found."));
        return gameMapper.toResponseDto(game);
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

        return gameMapper.toResponseDto(savedGame);
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

        return gameMapper.toResponseDto(existingGame);
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
}
