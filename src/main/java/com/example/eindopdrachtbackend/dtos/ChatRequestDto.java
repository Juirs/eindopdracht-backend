package com.example.eindopdrachtbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChatRequestDto {

    @NotBlank(message = "Recipient username is required")
    private String recipientUsername;

    @NotBlank(message = "Message content cannot be empty")
    @Size(max = 4000, message = "Message content cannot exceed 4000 characters")
    private String content;

    public ChatRequestDto() {
    }

    public ChatRequestDto(String recipientUsername, String content) {
        this.recipientUsername = recipientUsername;
        this.content = content;
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
}
