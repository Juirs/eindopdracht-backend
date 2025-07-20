package com.example.eindopdrachtbackend.mappers;

import com.example.eindopdrachtbackend.dtos.ReviewResponseDto;
import com.example.eindopdrachtbackend.models.GameReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponseDto toResponseDto(GameReview review) {
        if (review == null) {
            return null;
        }

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
