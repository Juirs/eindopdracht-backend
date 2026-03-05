package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.UserRequestDto;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerRegistrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterDeveloper() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername("testdev");
        dto.setPassword("password123");
        dto.setEmail("testdev@example.com");

        MvcResult result = mockMvc.perform(post("/users/register")
                        .param("isDeveloper", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        System.out.println("Response Status: " + result.getResponse().getStatus());
        System.out.println("Response Content: " + result.getResponse().getContentAsString());

        // Check if user was created
        Optional<User> userOpt = userRepository.findById("testdev");
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found in DB: " + user.getUsername());
            System.out.println("User roles: " + user.getRoles());
        } else {
            System.out.println("User NOT found in DB");
        }
    }
}
