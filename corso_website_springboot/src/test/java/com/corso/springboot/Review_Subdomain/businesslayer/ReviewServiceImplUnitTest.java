package com.corso.springboot.Review_Subdomain.businesslayer;


import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewIdentifier;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import com.corso.springboot.utils.exceptions.Review.ReviewsNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReviewServiceImplUnitTest {


    LocalDate date = LocalDate.of(2020, 10, 10);
    LocalDate date2 = LocalDate.of(2023, 2, 3);


    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewResponseMapper reviewResponseMapper;

    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;

    @Mock
    private ReviewService reviewService;


    @Test
    void getAllReviews() {

        List<ReviewResponse> mockReviewsResponse = List.of(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true)

        );

        Review review1 = Review.builder()
                .id(1)
                .reviewId(new ReviewIdentifier())
                .customerFullName("John Doe")
                .reviewDate(date)
                .message("This is a review")
                .reviewRating(3)
                .pinned(false)
                .build();

        Review review2 = Review.builder()
                .id(2)
                .reviewId(new ReviewIdentifier())
                .customerFullName("Emily Doe")
                .reviewDate(date2)
                .message("This is a review")
                .reviewRating(5)
                .pinned(true)
                .build();

        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));

        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);


        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);

        // Act
        List<ReviewResponse> reviewResult = reviewServiceImpl.getAllReviews();

        // Assert
        assertEquals(mockReviewsResponse, reviewResult);
        Mockito.verify(reviewRepository, Mockito.times(1)).findAll();
        Mockito.verify(reviewResponseMapper, Mockito.times(1)).toReviewsResponse(Mockito.anyList());

    }
    @Test
    void getReviewsByCustomerFullName_ShouldReturnReviews() {
        // Arrange
        String customerFullName = "John Doe";
        List<ReviewResponse> mockReviews = Arrays.asList(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true));

        when(reviewRepository.findAllByCustomerFullNameStartingWith(customerFullName))
                .thenReturn(Arrays.asList(
                        new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
                        new Review(1, new ReviewIdentifier(), "81893daAs", "Emily Doe", date, "This is a review", 3, true)
                ));

        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList()))
                .thenReturn(mockReviews);

        // Act
        List<ReviewResponse> result = reviewServiceImpl.getReviewsByCustomerFullName(customerFullName);

        // Assert
        assertEquals(mockReviews, result);

    }

    @Test
    void getReviewsByCustomerFullName_ShouldReturnAll() {
        // Arrange
        String customerFullName = null;
        List<ReviewResponse> mockReviews = Arrays.asList(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true));

        when(reviewRepository.findAllByCustomerFullNameStartingWith(customerFullName))
                .thenReturn(Arrays.asList(
                        new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
                        new Review(1, new ReviewIdentifier(), "81893daAs", "Emily Doe", date, "This is a review", 3, true)
                ));

        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList()))
                .thenReturn(mockReviews);

        // Act
        List<ReviewResponse> result = reviewServiceImpl.getReviewsByCustomerFullName(customerFullName);

        // Assert
        assertEquals(mockReviews, result);

    }

    @Test
    void getPinnedReviews() {

        List<ReviewResponse> mockReviewsResponse = List.of(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true)

        );


        Review review1 = Review.builder()
                .id(1)
                .reviewId(new ReviewIdentifier())
                .customerFullName("John Doe")
                .reviewDate(date)
                .message("This is a review")
                .reviewRating(3)
                .pinned(false)
                .build();

        Review review2 = Review.builder()
                .id(2)
                .reviewId(new ReviewIdentifier())
                .customerFullName("Emily Doe")
                .reviewDate(date2)
                .message("This is a review")
                .reviewRating(5)
                .pinned(true)
                .build();

        when(reviewRepository.getReviewByPinned(review1.pinned)).thenReturn(Arrays.asList(review1, review2));

        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);

    }

    @Test
    void getReviewsByUserId_ShouldSucceed() {
        // Arrange
        String userId = "google|123456789";

        List<ReviewResponse> mockReviewsResponses = Arrays.asList(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true));

        List<Review> mockReviews = Arrays.asList(
                new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
                new Review(1, new ReviewIdentifier(), "81893daAs", "Emily Doe", date, "This is a review", 3, true));

        when(reviewRepository.findAllByUserId(userId)).thenReturn(mockReviews);
        when(reviewResponseMapper.toReviewsResponse(mockReviews)).thenReturn(mockReviewsResponses);

        // Act
        List<ReviewResponse> result = reviewServiceImpl.getReviewsByUserId(userId);

        // Assert
        assertEquals(mockReviewsResponses, result);
        assertEquals(mockReviewsResponses.size(), result.size());
        assertEquals(mockReviewsResponses.get(0).getReviewId(), result.get(0).getReviewId());
        assertEquals(mockReviewsResponses.get(0).getReviewId(), result.get(0).getReviewId());
        assertEquals(mockReviewsResponses.get(0).getCustomerFullName(), result.get(0).getCustomerFullName());
        assertEquals(mockReviewsResponses.get(0).getReviewDate(), result.get(0).getReviewDate());
        assertEquals(mockReviewsResponses.get(0).getMessage(), result.get(0).getMessage());
        assertEquals(mockReviewsResponses.get(0).getReviewRating(), result.get(0).getReviewRating());
        assertEquals(mockReviewsResponses.get(0).isPinned(), result.get(0).isPinned());
        assertEquals(mockReviewsResponses.get(1).getReviewId(), result.get(1).getReviewId());
        assertEquals(mockReviewsResponses.get(1).getCustomerFullName(), result.get(1).getCustomerFullName());
        assertEquals(mockReviewsResponses.get(1).getReviewDate(), result.get(1).getReviewDate());
        assertEquals(mockReviewsResponses.get(1).getMessage(), result.get(1).getMessage());
        assertEquals(mockReviewsResponses.get(1).getReviewRating(), result.get(1).getReviewRating());
        assertEquals(mockReviewsResponses.get(1).isPinned(), result.get(1).isPinned());
    }

    @Test
    void getReviewsByUserId_ShouldThrowException() {
        // Arrange
        String userId = "google|123456789";

        when(reviewRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        // Act
        assertThrows(ReviewsNotFoundException.class, () -> reviewServiceImpl.getReviewsByUserId(userId));
    }


}






