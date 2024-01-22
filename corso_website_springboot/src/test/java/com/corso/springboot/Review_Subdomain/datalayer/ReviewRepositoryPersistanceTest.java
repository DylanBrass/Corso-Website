package com.corso.springboot.Review_Subdomain.datalayer;


import jnr.constants.platform.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
class ReviewRepositoryPersistanceTest {


    Logger log = Logger.getLogger(ReviewRepositoryPersistanceTest.class.getName());

    @Autowired
    ReviewRepository reviewRepository;


    @Test
    void findAllByCustomerFullNameStartingWith() {
        String customerFullName = "John Doe";
        List<Review> reviews = reviewRepository.findAllByCustomerFullNameStartingWithAndPagination(customerFullName, 10, 0);
        assertEquals(1, reviews.size());
    }


    @Test
    void reviewInLastWeekForUser_ThenReturnFalse() {
        String userId = "google|123456789";
        Boolean hasReviewed = reviewRepository.reviewInLastWeekForUser(userId);
        assertFalse(hasReviewed);
    }

    @Test
    void reviewInLastWeekForUser_ThenReturnTrue() {
        String userId = "google|123456789";

        Review review = new Review();
        review.setUserId(userId);
        review.setReviewId(new ReviewIdentifier());
        review.setCustomerFullName("John Doe");
        review.setReviewDate(LocalDate.now());
        review.setReviewRating(5);
        review.setPinned(false);
        review.setMessage("This is a test message");

        reviewRepository.save(review);


        Boolean hasReviewed = reviewRepository.reviewInLastWeekForUser(userId);
        assertTrue(hasReviewed);
    }

    @Test
    void getReviewByReviewIdAndUserId() {
        String userId = "auth0|65702e81e9661e14ab3aac89";
        String reviewId = "e2d2cb36-5a3e-4180-afdd-b416b66a4546";

        Review review1 = reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId);
        assertEquals(reviewId, review1.getReviewId().getReviewId());
    }



}