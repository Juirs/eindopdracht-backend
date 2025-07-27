package com.example.eindopdrachtbackend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllGames_ShouldReturnGamesList() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].category").exists());
    }

    @Test
    void testGetGameById_ShouldReturnSpecificGame() throws Exception {
        // Arrange
        Long gameId = 1L;

        // Act & Assert
        mockMvc.perform(get("/games/{id}", gameId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(gameId))
                .andExpect(jsonPath("$.title").value("Pixel Adventure"))
                .andExpect(jsonPath("$.category").value("PLATFORMER"))
                .andExpect(jsonPath("$.developerUsername").value("henk"));
    }
}
