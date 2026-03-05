package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.GameJamRequestDto;
import com.example.eindopdrachtbackend.dtos.GameJamResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class GameJamServiceIntegrationTest {

    @Autowired
    private GameJamService gameJamService;

    @Test
    @Transactional
    void createGameJam_WithLongDescription_ShouldSucceed() {
        // Arrange
        String longDescription = "A".repeat(300); // Longer than 255
        GameJamRequestDto requestDto = new GameJamRequestDto();
        requestDto.setName("Test Jam");
        requestDto.setDescription(longDescription);
        requestDto.setRules("Some rules");
        requestDto.setTheme("Retro");
        requestDto.setStartDate(LocalDateTime.now().plusDays(1));
        requestDto.setEndDate(LocalDateTime.now().plusDays(2));
        requestDto.setMaxParticipants(100);

        // Act
        GameJamResponseDto result = gameJamService.createGameJam(requestDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    @Transactional
    void createGameJam_WithLongImageUrl_ShouldSucceed() {
        // Arrange
        String longUrl = "http://example.com/images/" + "A".repeat(250) + ".png"; // Total length > 255
        GameJamRequestDto requestDto = new GameJamRequestDto();
        requestDto.setName("Test Jam URL");
        requestDto.setDescription("Description");
        requestDto.setRules("Rules");
        requestDto.setTheme("Modern");
        requestDto.setGameJamImageUrl(longUrl);
        requestDto.setStartDate(LocalDateTime.now().plusDays(1));
        requestDto.setEndDate(LocalDateTime.now().plusDays(2));
        requestDto.setMaxParticipants(100);

        // Act
        GameJamResponseDto result = gameJamService.createGameJam(requestDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
    }
    @Test
    @Transactional
    void createGameJam_WithVeryLongDescription_ShouldSucceed() {
        // Arrange
        String veryLongDescription = "A".repeat(1500); // Longer than previous 1000
        GameJamRequestDto requestDto = new GameJamRequestDto();
        requestDto.setName("Test Jam Large");
        requestDto.setDescription(veryLongDescription);
        requestDto.setRules("Rules");
        requestDto.setTheme("Retro");
        requestDto.setStartDate(LocalDateTime.now().plusDays(1));
        requestDto.setEndDate(LocalDateTime.now().plusDays(2));
        requestDto.setMaxParticipants(100);

        // Act
        GameJamResponseDto result = gameJamService.createGameJam(requestDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
    }
}
