package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.UserReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReviewVoteRepository extends JpaRepository<UserReviewVote, Long> {

    Optional<UserReviewVote> findByUsernameAndReviewId(String username, Long reviewId);
}
