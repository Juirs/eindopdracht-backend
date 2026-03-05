package com.example.eindopdrachtbackend.controllers;

import com.example.eindopdrachtbackend.dtos.FriendsListRequestDto;
import com.example.eindopdrachtbackend.dtos.FriendsListResponseDto;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.services.FriendsListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.eindopdrachtbackend.exceptions.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/friends")
public class FriendsListController {

    private final FriendsListService friendsListService;
    private final UserRepository userRepository;

    public FriendsListController(FriendsListService friendsListService, UserRepository userRepository) {
        this.friendsListService = friendsListService;
        this.userRepository = userRepository;
    }

    private User getUserFromPrincipal(UserDetails currentUser) {
        return userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUser.getUsername()));
    }

    @GetMapping
    public ResponseEntity<Set<FriendsListResponseDto>> getFriends(@AuthenticationPrincipal UserDetails currentUser) {
        User user = getUserFromPrincipal(currentUser);
        Set<FriendsListResponseDto> friends = friendsListService.getAcceptedFriends(user);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FriendsListResponseDto>> getAllFriendships(@AuthenticationPrincipal UserDetails currentUser) {
        List<FriendsListResponseDto> allFriends = friendsListService.getAllFriends();
        return ResponseEntity.ok(allFriends);
    }

    @GetMapping("/pending")
    public ResponseEntity<Set<FriendsListResponseDto>> getPendingRequests(@AuthenticationPrincipal UserDetails currentUser) {
        User user = getUserFromPrincipal(currentUser);
        Set<FriendsListResponseDto> pendingRequests = friendsListService.getPendingRequests(user);
        return ResponseEntity.ok(pendingRequests);
    }

    @PostMapping("/request")
    public ResponseEntity<FriendsListResponseDto> sendFriendRequest(
            @Valid @RequestBody FriendsListRequestDto requestDto,
            @AuthenticationPrincipal UserDetails currentUser) {
        
        User user = getUserFromPrincipal(currentUser);
        FriendsListResponseDto friendship = friendsListService.sendFriendRequest(user, requestDto);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(friendship.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(friendship);
    }

    @PutMapping("/accept")
    public ResponseEntity<FriendsListResponseDto> acceptFriendRequest(
            @Valid @RequestBody FriendsListRequestDto requestDto,
            @AuthenticationPrincipal UserDetails currentUser) {
        
        User user = getUserFromPrincipal(currentUser);
        FriendsListResponseDto friendship = friendsListService.acceptFriendRequest(user, requestDto);
        return ResponseEntity.ok(friendship);
    }

    @DeleteMapping("/decline")
    public ResponseEntity<FriendsListResponseDto> declineFriendRequest(
            @Valid @RequestBody FriendsListRequestDto requestDto,
            @AuthenticationPrincipal UserDetails currentUser) {
        
        User user = getUserFromPrincipal(currentUser);
        FriendsListResponseDto friendship = friendsListService.declineFriendRequest(user, requestDto);
        return ResponseEntity.ok(friendship);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<FriendsListResponseDto> removeFriend(
            @Valid @RequestBody FriendsListRequestDto requestDto,
            @AuthenticationPrincipal UserDetails currentUser) {
        
        User user = getUserFromPrincipal(currentUser);
        FriendsListResponseDto friendship = friendsListService.removeFriend(user, requestDto);
        return ResponseEntity.ok(friendship);
    }
}
