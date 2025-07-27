package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.GameJamRequestDto;
import com.example.eindopdrachtbackend.dtos.GameJamResponseDto;
import com.example.eindopdrachtbackend.exceptions.RecordNotFoundException;
import com.example.eindopdrachtbackend.mappers.GameJamMapper;
import com.example.eindopdrachtbackend.models.GameJam;
import com.example.eindopdrachtbackend.models.GameJamParticipant;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.GameJamRepository;
import com.example.eindopdrachtbackend.repositories.GameJamParticipantRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameJamService {
    private final GameJamRepository gameJamRepository;
    private final GameJamParticipantRepository gameJamParticipantRepository;
    private final UserRepository userRepository;
    private final GameJamMapper gameJamMapper;

    public GameJamService(GameJamRepository gameJamRepository,
                         GameJamParticipantRepository gameJamParticipantRepository,
                         UserRepository userRepository,
                         GameJamMapper gameJamMapper) {
        this.gameJamRepository = gameJamRepository;
        this.gameJamParticipantRepository = gameJamParticipantRepository;
        this.userRepository = userRepository;
        this.gameJamMapper = gameJamMapper;
    }

    public List<GameJamResponseDto> getAllGameJams() {
        return gameJamRepository.findAll().stream()
                .map(gameJamMapper::toResponseDto)
                .toList();
    }

    public GameJamResponseDto getGameJamById(Long id) {
        GameJam gameJam = gameJamRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Game Jam with ID " + id + " not found"));
        return gameJamMapper.toResponseDto(gameJam);
    }

    public GameJamResponseDto createGameJam(GameJamRequestDto requestDto) {
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        GameJam gameJam = new GameJam();
        gameJam.setName(requestDto.getName());
        gameJam.setDescription(requestDto.getDescription());
        gameJam.setRules(requestDto.getRules());
        gameJam.setTheme(requestDto.getTheme());
        gameJam.setGameJamImageUrl(requestDto.getGameJamImageUrl());
        gameJam.setStartDate(requestDto.getStartDate());
        gameJam.setEndDate(requestDto.getEndDate());
        gameJam.setMaxParticipants(requestDto.getMaxParticipants());
        gameJam.setCurrentParticipants(0);
        gameJam.setActive(true);

        GameJam savedGameJam = gameJamRepository.save(gameJam);
        return gameJamMapper.toResponseDto(savedGameJam);
    }

    public GameJamResponseDto joinGameJam(Long gameJamId, UserDetails currentUser) {
        GameJam gameJam = gameJamRepository.findById(gameJamId)
                .orElseThrow(() -> new RecordNotFoundException("Game Jam with ID " + gameJamId + " not found"));

        String username = currentUser.getUsername();

        if (gameJamParticipantRepository.findByGameJamIdAndUserUsername(gameJamId, username).isPresent()) {
            throw new RuntimeException("You have already joined this game jam");
        }

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        GameJamParticipant participant = new GameJamParticipant(gameJam, user);
        gameJamParticipantRepository.save(participant);

        gameJam.setCurrentParticipants(gameJam.getCurrentParticipants() + 1);
        gameJamRepository.save(gameJam);

        return gameJamMapper.toResponseDto(gameJam);
    }
}
