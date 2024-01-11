package com.corso.springboot.Review_Subdomain.businesslayer;


import com.corso.springboot.FAQ_Subdomain.datalayer.FAQ;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQIdentifier;
import com.corso.springboot.FAQ_Subdomain.presentationlayer.FAQResponse;
import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewIdentifier;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

        Mockito.when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));

        Mockito.when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);


        Mockito.when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);

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

        Mockito.when(reviewRepository.findAllByCustomerFullNameStartingWith(customerFullName))
                .thenReturn(Arrays.asList(
                        new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
                        new Review(1, new ReviewIdentifier(), "81893daAs", "Emily Doe", date, "This is a review", 3, true)
                ));

        Mockito.when(reviewResponseMapper.toReviewsResponse(Mockito.anyList()))
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

        Mockito.when(reviewRepository.findAllByCustomerFullNameStartingWith(customerFullName))
                .thenReturn(Arrays.asList(
                        new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
                        new Review(1, new ReviewIdentifier(), "81893daAs", "Emily Doe", date, "This is a review", 3, true)
                ));

        Mockito.when(reviewResponseMapper.toReviewsResponse(Mockito.anyList()))
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

        Mockito.when(reviewRepository.getReviewByPinned(review1.pinned)).thenReturn(Arrays.asList(review1, review2));

        Mockito.when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);

    }


}






