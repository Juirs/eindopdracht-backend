package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.GameRequestDto;
import com.example.eindopdrachtbackend.dtos.GameResponseDto;
import com.example.eindopdrachtbackend.dtos.ReviewRequestDto;
import com.example.eindopdrachtbackend.dtos.ReviewResponseDto;
import com.example.eindopdrachtbackend.models.GameGenre;
import com.example.eindopdrachtbackend.services.GameService;
import com.example.eindopdrachtbackend.services.ReviewService;
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
    private final ReviewService reviewService;

    public GameController(GameService gameService, ReviewService reviewService) {
        this.gameService = gameService;
        this.reviewService = reviewService;
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

    @GetMapping("/{gameId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsForGame(@PathVariable Long gameId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsForGame(gameId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{gameId}/reviews")
    public ResponseEntity<ReviewResponseDto> createReview(@PathVariable Long gameId,
                                                         @Valid @RequestBody ReviewRequestDto reviewRequestDto,
                                                         @AuthenticationPrincipal UserDetails currentUser) {
        ReviewResponseDto reviewResponse = reviewService.createReview(gameId, reviewRequestDto, currentUser);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reviewResponse.getId())
                .toUri();

        return ResponseEntity.created(uri).body(reviewResponse);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId,
                                            @AuthenticationPrincipal UserDetails currentUser) {
        reviewService.deleteReview(reviewId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reviews/{reviewId}/upvote")
    public ResponseEntity<ReviewResponseDto> upvoteReview(@PathVariable Long reviewId,
                                                         @AuthenticationPrincipal UserDetails currentUser) {
        ReviewResponseDto updatedReview = reviewService.upvoteReview(reviewId, currentUser);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping("/reviews/{reviewId}/downvote")
    public ResponseEntity<ReviewResponseDto> downvoteReview(@PathVariable Long reviewId,
                                                           @AuthenticationPrincipal UserDetails currentUser) {
        ReviewResponseDto updatedReview = reviewService.downvoteReview(reviewId, currentUser);
        return ResponseEntity.ok(updatedReview);
    }
}
