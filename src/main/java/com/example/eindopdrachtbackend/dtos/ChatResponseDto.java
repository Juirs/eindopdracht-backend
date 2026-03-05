package com.example.eindopdrachtbackend.dtos;

import java.time.LocalDateTime;

public class ChatResponseDto {

    private Long id;
    private String senderUsername;
    private String recipientUsername;
    private String content;
    private LocalDateTime sentAt;

    public ChatResponseDto() {
    }

    public ChatResponseDto(Long id, String senderUsername, String recipientUsername, String content, LocalDateTime sentAt) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.content = content;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
