package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.ChatResponseDto;
import com.example.eindopdrachtbackend.models.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMapper {

    public ChatResponseDto toResponseDto(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }

        ChatResponseDto dto = new ChatResponseDto();
        dto.setId(chatMessage.getId());
        dto.setSenderUsername(chatMessage.getSenderUsername());
        dto.setRecipientUsername(chatMessage.getRecipientUsername());
        dto.setContent(chatMessage.getContent());
        dto.setSentAt(chatMessage.getSentAt());

        return dto;
    }

    public List<ChatResponseDto> toResponseDtoList(List<ChatMessage> chatMessages) {
        if (chatMessages == null) {
            return null;
        }

        return chatMessages.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
