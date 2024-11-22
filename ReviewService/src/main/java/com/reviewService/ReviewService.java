package com.reviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public void createReview(ReviewRequest reviewRequest) {
        try {
            Review review = Review.builder()
                    .userName(reviewRequest.userName())
                    .content(reviewRequest.content())
                    .rating(reviewRequest.rating())
                    .build();
            reviewRepository.save(review);
            log.info("Review {} is saved", review.getId());
        } catch (Exception e) {
            log.error("Failed to save review: ", e);
        }
    }

    public List<ReviewResponse> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::mapToReviewResponse).toList();
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return new ReviewResponse(review.getId(), review.getUserName(),
                review.getContent(), review.getRating());
    }

    public void updateReview(Long id, ReviewRequest reviewRequest) {
        try {
            Review existingReview = reviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found"));

            existingReview.setUserName(reviewRequest.userName());
            existingReview.setContent(reviewRequest.content());
            existingReview.setRating(reviewRequest.rating());

            reviewRepository.save(existingReview);
            log.info("Review {} is updated", existingReview.getId());
        } catch (Exception e) {
            log.error("Failed to update review: ", e);
        }
    }

    public void deleteReview(Long id) {
        try {
            reviewRepository.deleteById(id);
            log.info("Review {} is deleted", id);
        } catch (Exception e) {
            log.error("Failed to delete review: ", e);
        }
    }

    public Integer getReviewRating(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isPresent()) {
            return review.get().getRating();
        } else {
            throw new RuntimeException("Rating not found");
        }
    }




}
