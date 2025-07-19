package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.GameRequestDto;
import com.example.eindopdrachtbackend.dtos.GameResponseDto;
import com.example.eindopdrachtbackend.models.GameGenre;
import com.example.eindopdrachtbackend.services.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping()
    public ResponseEntity<GameResponseDto> createGame(@Valid @RequestBody GameRequestDto gameRequestDto,
                                                     @AuthenticationPrincipal UserDetails currentUser) {

        GameResponseDto gameResponse = gameService.createGame(gameRequestDto, currentUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gameResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(gameResponse);
    }

    @GetMapping()
    public ResponseEntity<List<GameResponseDto>> getAllGamesOrByCategory(@RequestParam(required = false) GameGenre category) {
        List<GameResponseDto> gameResponses = gameService.getAllGamesOrByCategory(category);
        return ResponseEntity.ok(gameResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getGameById(@PathVariable Long id) {
        GameResponseDto gameResponse = gameService.getGameById(id);
        return ResponseEntity.ok(gameResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDto> updateGame(@PathVariable Long id,
                                                      @Valid @RequestBody GameRequestDto gameRequestDto,
                                                      @AuthenticationPrincipal UserDetails currentUser) {
        GameResponseDto updatedGame = gameService.updateGame(id, gameRequestDto, currentUser);
        return ResponseEntity.ok(updatedGame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        gameService.deleteGame(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
