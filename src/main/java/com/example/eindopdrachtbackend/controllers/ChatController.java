package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.ChatRequestDto;
import com.example.eindopdrachtbackend.dtos.ChatResponseDto;
import com.example.eindopdrachtbackend.models.ChatMessage;
import com.example.eindopdrachtbackend.services.ChatMessageService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat.send")
    public void handleChat(@Valid ChatRequestDto request, Principal principal) {
        String sender = principal.getName();

        ChatMessage msg = new ChatMessage();
        msg.setSenderUsername(sender);
        msg.setRecipientUsername(request.getRecipientUsername());
        msg.setContent(request.getContent());
        msg.setSentAt(LocalDateTime.now());

        ChatResponseDto savedDto = chatMessageService.saveMessage(msg);

        // Send to recipient
        messagingTemplate.convertAndSendToUser(
                savedDto.getRecipientUsername(), "/queue/messages", savedDto
        );
        // Also send to sender for echoing in their chat view
        messagingTemplate.convertAndSendToUser(
                savedDto.getSenderUsername(), "/queue/messages", savedDto
        );
    }
}
