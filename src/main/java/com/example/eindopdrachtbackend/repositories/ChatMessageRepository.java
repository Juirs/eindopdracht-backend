package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT c FROM ChatMessage c WHERE " +
            "(c.senderUsername = :user1 AND c.recipientUsername = :user2) OR " +
            "(c.senderUsername = :user2 AND c.recipientUsername = :user1) " +
            "ORDER BY c.sentAt ASC")
    List<ChatMessage> findConversation(@Param("user1") String user1, @Param("user2") String user2);
}
