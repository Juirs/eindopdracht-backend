package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.models.ChatMessage;
import com.example.eindopdrachtbackend.repositories.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChatHistoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void setUp() {
        chatMessageRepository.deleteAll();
        
        ChatMessage msg1 = new ChatMessage("henk", "jim", "Hello Jim!", LocalDateTime.now());
        ChatMessage msg2 = new ChatMessage("jim", "henk", "Hi Henk!", LocalDateTime.now());
        
        chatMessageRepository.save(msg1);
        chatMessageRepository.save(msg2);
    }

    @Test
    @WithMockUser(username = "henk")
    void testGetConversation_ShouldReturnHistory() throws Exception {
        mockMvc.perform(get("/chat/with/jim"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].senderUsername").value("henk"))
                .andExpect(jsonPath("$[0].recipientUsername").value("jim"))
                .andExpect(jsonPath("$[0].content").value("Hello Jim!"))
                .andExpect(jsonPath("$[1].senderUsername").value("jim"))
                .andExpect(jsonPath("$[1].recipientUsername").value("henk"))
                .andExpect(jsonPath("$[1].content").value("Hi Henk!"));
    }
}
