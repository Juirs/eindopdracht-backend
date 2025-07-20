package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.GameJamRequestDto;
import com.example.eindopdrachtbackend.dtos.GameJamResponseDto;
import com.example.eindopdrachtbackend.services.GameJamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/gamejams")
public class GameJamController {

    private final GameJamService gameJamService;

    public GameJamController(GameJamService gameJamService) {
        this.gameJamService = gameJamService;
    }

    @GetMapping
    public ResponseEntity<List<GameJamResponseDto>> getAllGameJams() {
        List<GameJamResponseDto> gameJams = gameJamService.getAllGameJams();
        return ResponseEntity.ok(gameJams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameJamResponseDto> getGameJamById(@PathVariable Long id) {
        GameJamResponseDto gameJam = gameJamService.getGameJamById(id);
        return ResponseEntity.ok(gameJam);
    }

    @PostMapping
    public ResponseEntity<GameJamResponseDto> createGameJam(@Valid @RequestBody GameJamRequestDto requestDto) {
        GameJamResponseDto createdGameJam = gameJamService.createGameJam(requestDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdGameJam.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdGameJam);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<GameJamResponseDto> joinGameJam(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails currentUser) {
        GameJamResponseDto gameJam = gameJamService.joinGameJam(id, currentUser);
        return ResponseEntity.ok(gameJam);
    }

}
