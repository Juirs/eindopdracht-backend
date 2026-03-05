package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.ChatResponseDto;
import com.example.eindopdrachtbackend.services.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatHistoryController {

    private final ChatMessageService chatMessageService;

    public ChatHistoryController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping("/with/{username}")
    public ResponseEntity<List<ChatResponseDto>> getConversation(@PathVariable String username, Authentication authentication) {
        String currentUser = authentication.getName();
        List<ChatResponseDto> history = chatMessageService.getConversationHistory(currentUser, username);
        return ResponseEntity.ok(history);
    }
}
