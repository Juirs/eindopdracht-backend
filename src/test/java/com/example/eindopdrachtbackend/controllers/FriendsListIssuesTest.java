package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.FriendsListRequestDto;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.FriendsListRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class FriendsListIssuesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsListRepository friendsListRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User userHenk;
    private User userJim;

    @BeforeEach
    void setUp() {
        friendsListRepository.deleteAll();

        userHenk = new User();
        userHenk.setUsername("henk_issue");
        userHenk.setPassword("password");
        userHenk.setEmail("henk_issue@example.com");
        userRepository.save(userHenk);

        userJim = new User();
        userJim.setUsername("jim_issue");
        userJim.setPassword("password");
        userJim.setEmail("jim_issue@example.com");
        userRepository.save(userJim);
    }

    @Test
    @WithMockUser(username = "jim_issue")
    void getPendingRequests_ShouldShowSenderUsername() throws Exception {
        // Henk sends a friend request to Jim
        com.example.eindopdrachtbackend.models.FriendsList friendship = 
            new com.example.eindopdrachtbackend.models.FriendsList(userHenk, userJim, "PENDING");
        friendsListRepository.save(friendship);

        // Jim checks his pending requests
        mockMvc.perform(get("/friends/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("henk_issue"))); // This is expected to fail and show "jim_issue" instead
    }

    @Test
    @WithMockUser(username = "jim_issue")
    void acceptFriendRequest_WithCorrectSenderUsername_ShouldSucceed() throws Exception {
        // Henk sends a friend request to Jim
        com.example.eindopdrachtbackend.models.FriendsList friendship = 
            new com.example.eindopdrachtbackend.models.FriendsList(userHenk, userJim, "PENDING");
        friendsListRepository.save(friendship);

        FriendsListRequestDto requestDto = new FriendsListRequestDto();
        requestDto.setFriendUsername("henk_issue");

        mockMvc.perform(put("/friends/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACCEPTED")));
    }

    @Test
    @WithMockUser(username = "jim_issue")
    void acceptFriendRequest_WithIncorrectSenderUsername_ShouldFailWith500() throws Exception {
        // Henk sends a friend request to Jim
        com.example.eindopdrachtbackend.models.FriendsList friendship = 
            new com.example.eindopdrachtbackend.models.FriendsList(userHenk, userJim, "PENDING");
        friendsListRepository.save(friendship);

        FriendsListRequestDto requestDto = new FriendsListRequestDto();
        requestDto.setFriendUsername("jim_issue"); // Wrong: jim is the receiver, not the sender

        mockMvc.perform(put("/friends/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound()); // Changed from 500 to 404 because of exception handler
    }
}
