package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.FriendsList;
import com.example.eindopdrachtbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsListRepository extends JpaRepository<FriendsList, Long> {
    
    List<FriendsList> findByUserAndStatus(User user, String status);
    
    List<FriendsList> findByFriendAndStatus(User friend, String status);
    
    Optional<FriendsList> findByUserAndFriend(User user, User friend);
    
    boolean existsByUserAndFriend(User user, User friend);
}
