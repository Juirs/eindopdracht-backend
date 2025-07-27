package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.GameRequestDto;
import com.example.eindopdrachtbackend.dtos.GameResponseDto;
import com.example.eindopdrachtbackend.exceptions.NameTooLongException;
import com.example.eindopdrachtbackend.exceptions.RecordNotFoundException;
import com.example.eindopdrachtbackend.mappers.GameMapper;
import com.example.eindopdrachtbackend.models.Game;
import com.example.eindopdrachtbackend.models.GameGenre;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.GameJamParticipantRepository;
import com.example.eindopdrachtbackend.repositories.GameRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.utils.SecurityUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;
    private final SimpleFileService simpleFileService;
    private final GameJamParticipantRepository gameJamParticipantRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository, GameMapper gameMapper,
                      SimpleFileService simpleFileService, GameJamParticipantRepository gameJamParticipantRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameMapper = gameMapper;
        this.simpleFileService = simpleFileService;
        this.gameJamParticipantRepository = gameJamParticipantRepository;
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
        game.setGameFilePath(gameRequestDto.getGameFilePath());
        game.setImageUrl(gameRequestDto.getImageUrl());
        game.setTrailerUrl(gameRequestDto.getTrailerUrl());

        if (gameRequestDto.getScreenshots() != null) {
            game.setScreenshots(gameRequestDto.getScreenshots());
        }

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
        existingGame.setGameFilePath(gameRequestDto.getGameFilePath());
        existingGame.setImageUrl(gameRequestDto.getImageUrl());
        existingGame.setTrailerUrl(gameRequestDto.getTrailerUrl());

        if (gameRequestDto.getScreenshots() != null) {
            existingGame.setScreenshots(gameRequestDto.getScreenshots());
        }

        gameRepository.save(existingGame);
        return gameMapper.toResponseDto(existingGame);
    }

    public GameResponseDto uploadGameImage(Long gameId, MultipartFile imageFile, UserDetails currentUser) throws IOException {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + gameId + " not found."));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, game.getDeveloper().getUsername())) {
            throw new AccessDeniedException("You can only update your own games");
        }

        if (game.getImageUrl() != null && !game.getImageUrl().isEmpty()) {
            simpleFileService.deleteFile(game.getImageUrl());
        }

        String imagePath = simpleFileService.saveGameImage(imageFile);
        game.setImageUrl(imagePath);

        Game savedGame = gameRepository.save(game);
        return gameMapper.toResponseDto(savedGame);
    }

    public GameResponseDto uploadGameFile(Long gameId, MultipartFile gameFile, UserDetails currentUser) throws IOException {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + gameId + " not found."));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, game.getDeveloper().getUsername())) {
            throw new AccessDeniedException("You can only update your own games");
        }

        if (game.getGameFilePath() != null && !game.getGameFilePath().isEmpty()) {
            simpleFileService.deleteFile(game.getGameFilePath());
        }

        String gameFilePath = simpleFileService.saveGameFile(gameFile);
        game.setGameFilePath(gameFilePath);

        Game savedGame = gameRepository.save(game);
        return gameMapper.toResponseDto(savedGame);
    }

    @Transactional
    public void deleteGame(Long id, UserDetails currentUser) {
        if (id == null) {
            throw new RecordNotFoundException("Game ID cannot be null.");
        }

        Game gameToDelete = gameRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + id + " not found."));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, gameToDelete.getDeveloper().getUsername())) {
            throw new AccessDeniedException("You can only delete your own games");
        }

        gameJamParticipantRepository.removeGameFromSubmissions(id);

        if (gameToDelete.getImageUrl() != null && !gameToDelete.getImageUrl().isEmpty()) {
            simpleFileService.deleteFile(gameToDelete.getImageUrl());
        }
        if (gameToDelete.getGameFilePath() != null && !gameToDelete.getGameFilePath().isEmpty()) {
            simpleFileService.deleteFile(gameToDelete.getGameFilePath());
        }

        if (gameToDelete.getScreenshots() != null) {
            for (String screenshotPath : gameToDelete.getScreenshots()) {
                simpleFileService.deleteFile(screenshotPath);
            }
        }

        gameRepository.deleteById(id);
    }

    public GameResponseDto createGameWithFiles(String title, String description, GameGenre category,
                                             String trailerUrl, MultipartFile imageFile, MultipartFile gameFile,
                                             List<MultipartFile> screenshotFiles, UserDetails currentUser) throws IOException {
        GameRequestDto gameRequestDto = new GameRequestDto();
        gameRequestDto.setTitle(title);
        gameRequestDto.setDescription(description);
        gameRequestDto.setCategory(category);
        gameRequestDto.setTrailerUrl(trailerUrl);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = simpleFileService.saveGameImage(imageFile);
            gameRequestDto.setImageUrl(imagePath);
        }

        if (gameFile != null && !gameFile.isEmpty()) {
            String gameFilePath = simpleFileService.saveGameFile(gameFile);
            gameRequestDto.setGameFilePath(gameFilePath);
        }

        if (screenshotFiles != null && !screenshotFiles.isEmpty()) {
            List<String> screenshotPaths = new ArrayList<>();
            for (int i = 0; i < Math.min(screenshotFiles.size(), 4); i++) {
                MultipartFile screenshotFile = screenshotFiles.get(i);
                if (screenshotFile != null && !screenshotFile.isEmpty()) {
                    String screenshotPath = simpleFileService.saveGameImage(screenshotFile);
                    screenshotPaths.add(screenshotPath);
                }
            }
            gameRequestDto.setScreenshots(screenshotPaths);
        }

        return createGame(gameRequestDto, currentUser);
    }

    public GameResponseDto uploadScreenshots(Long gameId, List<MultipartFile> screenshotFiles,
                                           UserDetails currentUser) throws IOException {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + gameId + " not found"));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, game.getDeveloper().getUsername())) {
            throw new AccessDeniedException("You can only update your own games");
        }

        if (game.getScreenshots() != null) {
            for (String screenshotPath : game.getScreenshots()) {
                simpleFileService.deleteFile(screenshotPath);
            }
        }

        List<String> screenshotPaths = new ArrayList<>();
        if (screenshotFiles != null && !screenshotFiles.isEmpty()) {
            for (int i = 0; i < Math.min(screenshotFiles.size(), 4); i++) {
                MultipartFile screenshotFile = screenshotFiles.get(i);
                if (screenshotFile != null && !screenshotFile.isEmpty()) {
                    String screenshotPath = simpleFileService.saveGameImage(screenshotFile);
                    screenshotPaths.add(screenshotPath);
                }
            }
        }

        game.setScreenshots(screenshotPaths);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toResponseDto(savedGame);
    }

    public ResponseEntity<Resource> getGameImageResource(Long id) throws IOException {
        GameResponseDto game = getGameById(id);

        if (game.getImageUrl() == null || game.getImageUrl().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (isExternalUrl(game.getImageUrl())) {
            return ResponseEntity.status(302)
                    .header("Location", game.getImageUrl())
                    .build();
        }

        return getFileResource(game.getImageUrl());
    }

    public ResponseEntity<Resource> getGameFileResource(Long id) throws IOException {
        GameResponseDto game = getGameById(id);

        if (game.getGameFilePath() == null || game.getGameFilePath().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Path path = simpleFileService.getFilePath(game.getGameFilePath());

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        String filename = path.getFileName().toString();
        String originalFilename = filename;
        if (filename.contains("_")) {
            originalFilename = filename.substring(filename.indexOf('_') + 1);
        }

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFilename + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(path)))
                .body(resource);
    }

    public ResponseEntity<Resource> getGameScreenshotResource(Long id, int screenshotIndex) throws IOException {
        GameResponseDto game = getGameById(id);

        if (game.getScreenshots() == null || game.getScreenshots().isEmpty() ||
            screenshotIndex < 0 || screenshotIndex >= game.getScreenshots().size()) {
            return ResponseEntity.notFound().build();
        }

        String screenshotPath = game.getScreenshots().get(screenshotIndex);

        if (isExternalUrl(screenshotPath)) {
            return ResponseEntity.status(302)
                    .header("Location", screenshotPath)
                    .build();
        }

        return getFileResource(screenshotPath);
    }

    private boolean isExternalUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    private ResponseEntity<Resource> getFileResource(String relativePath) throws IOException {
        Path path = simpleFileService.getFilePath(relativePath);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }
}
