package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.FriendsListRequestDto;
import com.example.eindopdrachtbackend.dtos.FriendsListResponseDto;
import com.example.eindopdrachtbackend.exceptions.FriendshipAlreadyExistsException;
import com.example.eindopdrachtbackend.exceptions.FriendshipNotFoundException;
import com.example.eindopdrachtbackend.mappers.FriendsListMapper;
import com.example.eindopdrachtbackend.models.FriendsList;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.repositories.FriendsListRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendsListService {
    private final FriendsListRepository friendsListRepository;
    private final FriendsListMapper friendsListMapper;
    private final UserRepository userRepository;

    public FriendsListService(FriendsListRepository friendsListRepository, FriendsListMapper friendsListMapper, UserRepository userRepository) {
        this.friendsListRepository = friendsListRepository;
        this.friendsListMapper = friendsListMapper;
        this.userRepository = userRepository;
    }

    public List<FriendsListResponseDto> getAllFriends() {
        return friendsListRepository.findAll().stream()
                .map(friendsListMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    public Set<FriendsListResponseDto> getAcceptedFriends(User user) {
        List<FriendsList> friendships1 = friendsListRepository.findByUserAndStatus(user, "ACCEPTED");
        List<FriendsList> friendships2 = friendsListRepository.findByFriendAndStatus(user, "ACCEPTED");
        
        Set<FriendsListResponseDto> result = friendships1.stream()
                .map(f -> friendsListMapper.toResponseDto(f, user.getUsername()))
                .collect(Collectors.toSet());
        
        result.addAll(friendships2.stream()
                .map(f -> friendsListMapper.toResponseDto(f, user.getUsername()))
                .collect(Collectors.toSet()));
        
        return result;
    }
    
    public Set<FriendsListResponseDto> getPendingRequests(User user) {
        List<FriendsList> friendships = friendsListRepository.findByFriendAndStatus(user, "PENDING");
        return friendships.stream()
                .map(f -> friendsListMapper.toResponseDto(f, user.getUsername()))
                .collect(Collectors.toSet());
    }
    
    public FriendsListResponseDto sendFriendRequest(User user, FriendsListRequestDto requestDto) {
        User friend = findUserByUsername(requestDto.getFriendUsername());
        
        FriendsList friendship = sendFriendRequestInternal(user, friend);
        return friendsListMapper.toResponseDto(friendship, user.getUsername());
    }
    
    public FriendsListResponseDto acceptFriendRequest(User user, FriendsListRequestDto requestDto) {
        User friend = findUserByUsername(requestDto.getFriendUsername());
        
        FriendsList friendship = acceptFriendRequestInternal(user, friend);
        return friendsListMapper.toResponseDto(friendship, user.getUsername());
    }
    
    public FriendsListResponseDto declineFriendRequest(User user, FriendsListRequestDto requestDto) {
        User friend = findUserByUsername(requestDto.getFriendUsername());
        
        FriendsList friendship = friendsListRepository.findByUserAndFriend(friend, user)
                .orElseThrow(() -> new FriendshipNotFoundException("Friend request not found"));
        
        friendsListRepository.delete(friendship);
        return friendsListMapper.toResponseDto(friendship, user.getUsername());
    }
    
    public FriendsListResponseDto removeFriend(User user, FriendsListRequestDto requestDto) {
        User friend = findUserByUsername(requestDto.getFriendUsername());
        
        FriendsList friendship = friendsListRepository.findByUserAndFriend(user, friend)
                .or(() -> friendsListRepository.findByUserAndFriend(friend, user))
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));
        
        friendsListRepository.delete(friendship);
        return friendsListMapper.toResponseDto(friendship, user.getUsername());
    }
    
    // Private internal methods that work with User entities
    private FriendsList sendFriendRequestInternal(User user, User friend) {
        if (friendsListRepository.existsByUserAndFriend(user, friend) || 
            friendsListRepository.existsByUserAndFriend(friend, user)) {
            throw new FriendshipAlreadyExistsException("Friendship or request already exists");
        }
        
        FriendsList friendship = new FriendsList(user, friend, "PENDING");
        return friendsListRepository.save(friendship);
    }
    
    private FriendsList acceptFriendRequestInternal(User user, User friend) {
        FriendsList friendship = friendsListRepository.findByUserAndFriend(friend, user)
                .orElseThrow(() -> new FriendshipNotFoundException("Friend request not found"));
        
        friendship.setStatus("ACCEPTED");
        return friendsListRepository.save(friendship);
    }
    
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
