package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.ChatResponseDto;
import com.example.eindopdrachtbackend.mappers.ChatMapper;
import com.example.eindopdrachtbackend.models.ChatMessage;
import com.example.eindopdrachtbackend.repositories.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatMapper chatMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMapper = chatMapper;
    }

    @Transactional
    public ChatResponseDto saveMessage(ChatMessage chatMessage) {
        ChatMessage saved = chatMessageRepository.save(chatMessage);
        return chatMapper.toResponseDto(saved);
    }

    public List<ChatResponseDto> getConversationHistory(String user1, String user2) {
        List<ChatMessage> history = chatMessageRepository.findConversation(user1, user2);
        return chatMapper.toResponseDtoList(history);
    }
}
