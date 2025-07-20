package com.example.eindopdrachtbackend.services;

import com.example.eindopdrachtbackend.dtos.ReviewRequestDto;
import com.example.eindopdrachtbackend.dtos.ReviewResponseDto;
import com.example.eindopdrachtbackend.exceptions.BadRequestException;
import com.example.eindopdrachtbackend.exceptions.CannotReviewTwiceException;
import com.example.eindopdrachtbackend.exceptions.RecordNotFoundException;
import com.example.eindopdrachtbackend.models.Game;
import com.example.eindopdrachtbackend.models.GameReview;
import com.example.eindopdrachtbackend.models.User;
import com.example.eindopdrachtbackend.models.UserReviewVote;
import com.example.eindopdrachtbackend.models.VoteType;
import com.example.eindopdrachtbackend.repositories.GameRepository;
import com.example.eindopdrachtbackend.repositories.ReviewRepository;
import com.example.eindopdrachtbackend.repositories.UserRepository;
import com.example.eindopdrachtbackend.repositories.UserReviewVoteRepository;
import com.example.eindopdrachtbackend.utils.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final UserReviewVoteRepository userReviewVoteRepository;

    public ReviewService(ReviewRepository reviewRepository, GameRepository gameRepository,
                        UserRepository userRepository, UserReviewVoteRepository userReviewVoteRepository) {
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.userReviewVoteRepository = userReviewVoteRepository;
    }

    public List<ReviewResponseDto> getReviewsForGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + gameId + " not found"));

        return game.getReviews().stream()
                .map(ReviewService::fromReview)
                .toList();
    }

    public ReviewResponseDto createReview(Long gameId, ReviewRequestDto requestDto, UserDetails currentUser) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RecordNotFoundException("Game with ID " + gameId + " not found"));

        User reviewer = userRepository.findById(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with the name " + currentUser.getUsername() + " not found"));

        boolean hasReviewed = game.getReviews().stream()
                .anyMatch(review -> review.getReviewer().getUsername().equals(currentUser.getUsername()));

        if (hasReviewed) {
            throw new CannotReviewTwiceException();
        }

        GameReview review = new GameReview();
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setGame(game);
        review.setReviewer(reviewer);

        GameReview savedReview = reviewRepository.save(review);
        return fromReview(savedReview);
    }

    public void deleteReview(Long reviewId, UserDetails currentUser) {
        GameReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RecordNotFoundException("Review with ID " + reviewId + " not found"));

        if (!SecurityUtils.isOwnerOrAdmin(currentUser, review.getReviewer().getUsername())) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }

        reviewRepository.deleteById(reviewId);
    }

    public ReviewResponseDto upvoteReview(Long reviewId, UserDetails currentUser) {
        return voteOnReview(reviewId, currentUser, VoteType.UPVOTE);
    }

    public ReviewResponseDto downvoteReview(Long reviewId, UserDetails currentUser) {
        return voteOnReview(reviewId, currentUser, VoteType.DOWNVOTE);
    }

    private ReviewResponseDto voteOnReview(Long reviewId, UserDetails currentUser, VoteType newVoteType) {
        GameReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RecordNotFoundException("Review with ID " + reviewId + " not found"));

        String username = currentUser.getUsername();

        Optional<UserReviewVote> existingVote = userReviewVoteRepository.findByUsernameAndReviewId(username, reviewId);

        if (existingVote.isPresent()) {
            UserReviewVote vote = existingVote.get();

            if (vote.getVoteType() == newVoteType) {
                removeVote(review, vote.getVoteType());
                userReviewVoteRepository.delete(vote);
            }
            else {
                removeVote(review, vote.getVoteType());
                addVote(review, newVoteType);
                vote.setVoteType(newVoteType);
                userReviewVoteRepository.save(vote);
            }
        }
        else {
            addVote(review, newVoteType);
            userReviewVoteRepository.save(new UserReviewVote(username, reviewId, newVoteType));
        }

        GameReview updatedReview = reviewRepository.save(review);
        return fromReview(updatedReview);
    }

    private void addVote(GameReview review, VoteType voteType) {
        if (voteType == VoteType.UPVOTE) {
            review.addUpvote();
        } else {
            review.addDownvote();
        }
    }

    private void removeVote(GameReview review, VoteType voteType) {
        if (voteType == VoteType.UPVOTE) {
            review.removeUpvote();
        } else {
            review.removeDownvote();
        }
    }

    // Entity to DTO mapping
    public static ReviewResponseDto fromReview(GameReview review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewerUsername(review.getReviewer().getUsername());
        dto.setGameId(review.getGame().getId());
        dto.setGameTitle(review.getGame().getTitle());
        dto.setUpvotes(review.getUpvotes());
        dto.setDownvotes(review.getDownvotes());
        dto.setTotalScore(review.getTotalScore());

        if (review.getReviewer().getUserProfile() != null) {
            dto.setReviewerAvatar(review.getReviewer().getUserProfile().getAvatar());
        }

        return dto;
    }
}
