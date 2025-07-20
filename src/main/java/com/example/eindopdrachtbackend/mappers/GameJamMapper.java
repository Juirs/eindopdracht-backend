package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.GameJamResponseDto;
import com.example.eindopdrachtbackend.models.GameJam;
import org.springframework.stereotype.Component;

@Component
public class GameJamMapper {

    public GameJamResponseDto toResponseDto(GameJam gameJam) {
        if (gameJam == null) {
            return null;
        }

        GameJamResponseDto dto = new GameJamResponseDto();
        dto.setId(gameJam.getId());
        dto.setName(gameJam.getName());
        dto.setDescription(gameJam.getDescription());
        dto.setRules(gameJam.getRules());
        dto.setTheme(gameJam.getTheme());
        dto.setGameJamImageUrl(gameJam.getGameJamImageUrl());
        dto.setStartDate(gameJam.getStartDate());
        dto.setEndDate(gameJam.getEndDate());
        dto.setCreatedAt(gameJam.getCreatedAt());
        dto.setMaxParticipants(gameJam.getMaxParticipants());
        dto.setCurrentParticipants(gameJam.getCurrentParticipants());
        dto.setIsActive(gameJam.getActive());

        dto.setCurrentlyActive(gameJam.getActive());
        dto.setCanAcceptParticipants(gameJam.getActive() &&
                                   gameJam.getCurrentParticipants() < gameJam.getMaxParticipants());

        return dto;
    }
}
