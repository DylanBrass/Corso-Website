package com.corso.springboot.Review_Subdomain.businesslayer;


import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewIdentifier;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewUpdateRequest;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.Tools.BasicTools;
import com.corso.springboot.utils.exceptions.Review.*;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewUpdateRequest;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.exceptions.Review.ReviewsNotFoundException;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Mock
    private ReviewUpdateRequest reviewUpdateRequest;

    @Mock
    private CustomerService customerService;

    @Mock
    private Auth0ManagementService auth0ManagementService;

    @Mock
    private EmailService emailService;

    @Mock
    private OrderService orderService;
    @Mock
    private BasicTools basicTools;

    @Test
    void getAllReviews() {

        List<ReviewResponse> mockReviewsResponse = List.of(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
                        2),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true,
                        2)

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
        List<ReviewResponse> reviewResult = reviewServiceImpl.getReviews(Map.of(), 1, 1);

        // Assert
        assertEquals(mockReviewsResponse, reviewResult);
        verify(reviewRepository, Mockito.times(1)).findAllReviews(1, 1);
        verify(reviewResponseMapper, Mockito.times(1)).toReviewsResponse(Mockito.anyList());
    }

    @Test
    void getReviewsByCustomerFullName_ShouldReturnReviews() {
        // Arrange
        String customerFullName = "John Doe";
        List<ReviewResponse> mockReviews = Arrays.asList(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
                        2),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true,
                        2)

        );
        when(reviewRepository.findAllByCustomerFullNameStartingWithAndPagination(customerFullName, 1, 1))
                .thenReturn(Arrays.asList(
                        new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
                        new Review(2, new ReviewIdentifier(), "2nun21", "Jane Doe", date, "This is a review", 3, false)));

        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList()))
                .thenReturn(mockReviews);

        // Act
        List<ReviewResponse> result = reviewServiceImpl.getReviews(Map.of("customerFullName", customerFullName), 1, 1);

        // Assert
        assertEquals(mockReviews, result);

    }


//    @Test
//    void getReviewsByCustomerFullName_ShouldReturnAll(Map<String, String> params, int pageSize, int offset) {
//        // Arrange
//        String customerFullName = null;
//        List<ReviewResponse> mockReviews = Arrays.asList(
//                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
//                        2),
//                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true,
//                        2)
//
//        );
//        when(reviewRepository.findAllByCustomerFullNameStartingWithAndPagination(customerFullName, pageSize, offset))
//                .thenReturn(Arrays.asList(
//                        new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false),
//                        new Review(2, new ReviewIdentifier(), "2nun21", "Jane Doe", date, "This is a review", 3, false)));
//
//
//        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList()))
//                .thenReturn(mockReviews);
//
//        // Act
//        List<ReviewResponse> result = reviewServiceImpl.getReviews(params, pageSize, offset);
//
//        // Assert
//        assertEquals(mockReviews, result);
//
//    }
//
//    @Test
//    void getPinnedReviews() {
//
//        List<ReviewResponse> mockReviewsResponse = List.of(
//                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
//                        2),
//                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true,
//                        2)
//
//        );
//
//
//        Review review1 = Review.builder()
//                .id(1)
//                .reviewId(new ReviewIdentifier())
//                .customerFullName("John Doe")
//                .reviewDate(date)
//                .message("This is a review")
//                .reviewRating(3)
//                .pinned(false)
//                .build();
//
//        Review review2 = Review.builder()
//                .id(2)
//                .reviewId(new ReviewIdentifier())
//                .customerFullName("Emily Doe")
//                .reviewDate(date2)
//                .message("This is a review")
//                .reviewRating(5)
//                .pinned(true)
//                .build();
//
//        when(reviewRepository.getReviewByPinned(review1.pinned)).thenReturn(Arrays.asList(review1, review2));
//
//        when(reviewResponseMapper.toReviewsResponse(Mockito.anyList())).thenReturn(mockReviewsResponse);
//
//    }

    @Test
    void getReviewsByUserId_ShouldSucceed() {
        // Arrange
        String userId = "google|123456789";

        List<ReviewResponse> mockReviewsResponses = Arrays.asList(
                new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
                        2),
                new ReviewResponse("1", "2", "Emily Doe", date2, "This is a second review", 5, true,
                        2)

        );
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

    @Test
    void whenUserCreatesReviewAndHasNoOrders_ThenThrowUserHasNoOrdersException() {
        // Arrange
        String userId = "google|123456789";
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .reviewRating(5)
                .message("This is a test message")
                .customerFullName("John Doe")
                .build();

        when(orderService.findAnyUserOrders(userId)).thenReturn(false);


        // Act and Assert
        assertThrows(ReviewUserHasNoOrdersException.class, () -> reviewServiceImpl.customerCreateReview(userId, reviewRequest));
    }

    @Test
    void testDeleteCustomerReview() throws MessagingException {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";
        Review mockReview = new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", LocalDate.now(), "This is a review", 3, false);

        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(mockReview);

        // Act
        reviewServiceImpl.deleteCustomerReview(reviewId, userId);

        // Assert
        verify(reviewRepository, times(1)).findReviewByReviewId_ReviewIdAndUserId(reviewId, userId);
        verify(reviewRepository, times(1)).delete(mockReview);
    }

    @Test
    void deletePinnedCustomerReview() throws MessagingException {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";
        Review mockReview = new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", LocalDate.now(), "This is a review", 3, true);

        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(mockReview);

        List<UserInfoResponse> admins = new ArrayList<>();
        admins.add(new UserInfoResponse("auth0|65702e81e9661e14ab3aac89", "test@email.com",
                "Doe", new HashMap<>()));
        when(auth0ManagementService.getAllAdmins()).thenReturn(admins);
        doThrow(new MessagingException("Failed to send email")).when(basicTools).sendEmailToAdmins(any(String.class), any(String.class), any(Map.class));

        assertThrows(MessagingException.class, () -> reviewServiceImpl.deleteCustomerReview(reviewId, userId));

    }

    @Test
    void deleteCustomerReview_ShouldSendEmailToAdmins_WhenReviewIsPinned() throws MessagingException {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";
        Review mockReview = new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", LocalDate.now(), "This is a review", 3, true);

        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(mockReview);
        when(auth0ManagementService.getAllAdmins()).thenReturn(Collections.singletonList(
                new UserInfoResponse("auth0|adminId", "admin@example.com", "Admin", new HashMap<>())
        ));

        // Act
        reviewServiceImpl.deleteCustomerReview(reviewId, userId);

        // Assert
        verify(basicTools, times(1)).sendEmailToAdmins(any(String.class), any(String.class), any());
    }

    @Test
    void whenCustomerDeletesReview_ButReviewDoesNotExist_ThenThrowReviewNotFoundException() {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";

        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(null);

        // Act and Assert
        assertThrows(ReviewNotFoundException.class, () -> reviewServiceImpl.deleteCustomerReview(reviewId, userId));
    }

    @Test
    void getReviewByReviewId_ShouldReturnReview() {
        // Arrange
        String reviewId = "1";
        ReviewResponse mockReviewResponse = new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
                2);
        Review mockReview = new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false);

        when(reviewRepository.findReviewByReviewId_ReviewId(reviewId)).thenReturn(mockReview);
        when(reviewResponseMapper.toReviewResponse(mockReview)).thenReturn(mockReviewResponse);

        // Act
        ReviewResponse result = reviewServiceImpl.getReviewByReviewId(reviewId);

        // Assert
        assertEquals(mockReviewResponse, result);
        assertEquals(mockReviewResponse.getReviewId(), result.getReviewId());
        assertEquals(mockReviewResponse.getCustomerFullName(), result.getCustomerFullName());
        assertEquals(mockReviewResponse.getReviewDate(), result.getReviewDate());
        assertEquals(mockReviewResponse.getMessage(), result.getMessage());
        assertEquals(mockReviewResponse.getReviewRating(), result.getReviewRating());
        assertEquals(mockReviewResponse.isPinned(), result.isPinned());
    }

    @Test
    void getReviewByReviewId_ShouldThrowException() {
        // Arrange
        String reviewId = "1";

        // Mock behavior
        when(reviewRepository.findReviewByReviewId_ReviewId(reviewId)).thenReturn(null);

        // Act
        try {
            reviewServiceImpl.getReviewByReviewId(reviewId);
        } catch (Exception e) {
            System.out.println("Exception message: " + e.getMessage());
        }
    }

    @Test
    void getCountOfAllReviews() {
        // Arrange
        int expectedCount = 0;

        when(reviewRepository.countAllBy()).thenReturn(expectedCount);

        // Act
        int result = reviewServiceImpl.getCountAllReviews();

        // Assert
        assertEquals(expectedCount, result);
    }

    @Test
    void getCountOfAllReviewsByState() {
        // Arrange
        int expectedCount = 0;
        boolean state = true;

        when(reviewRepository.countAllByPinned(state)).thenReturn(expectedCount);

        // Act
        int result = reviewServiceImpl.getCountAllByState(state);

        // Assert
        assertEquals(expectedCount, result);
    }

    @Test
    void getReviewByReviewIdAndUserId_ShouldReturnReview() {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";
        ReviewResponse mockReviewResponse = new ReviewResponse("1", "2", "John Doe", date, "This is a review", 3, false,
                2);
        Review mockReview = new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", date, "This is a review", 3, false);

        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(mockReview);
        when(reviewResponseMapper.toReviewResponse(mockReview)).thenReturn(mockReviewResponse);

        // Act
        ReviewResponse result = reviewServiceImpl.getReviewByReviewIdAndUserId(reviewId, userId);

        // Assert
        assertEquals(mockReviewResponse, result);
        assertEquals(mockReviewResponse.getReviewId(), result.getReviewId());
        assertEquals(mockReviewResponse.getCustomerFullName(), result.getCustomerFullName());
        assertEquals(mockReviewResponse.getReviewDate(), result.getReviewDate());
        assertEquals(mockReviewResponse.getMessage(), result.getMessage());
        assertEquals(mockReviewResponse.getReviewRating(), result.getReviewRating());
        assertEquals(mockReviewResponse.isPinned(), result.isPinned());
    }

    @Test
    void getReviewByIdAndUserId_ShouldThrowException() {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";

        // Mock behavior
        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(null);

        assertThrows(ReviewNotFoundException.class, () -> reviewServiceImpl.getReviewByReviewIdAndUserId(reviewId, userId));
    }

    @Test
    void updateReviewWithInvalidId_ShouldThrowNotFOundException() {
        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";
        ReviewRequest reviewUpdateRequest = ReviewRequest.builder()
                .message("This is a test message")
                .customerFullName("John Doe")
                .reviewRating(5)
                .build();

        // Mock behavior
        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(null);

        // Act and Assert
        assertThrows(ReviewNotFoundException.class, () -> reviewServiceImpl.updateCustomerReview(reviewId, userId, reviewUpdateRequest));
    }


    @Test
    void updatePinnedReview_ShouldThrowBadRequestException() {

        // Arrange
        String reviewId = "1";
        String userId = "google|123456789";
        ReviewRequest reviewUpdateRequest = ReviewRequest.builder()
                .message("This is a test message")
                .customerFullName("John Doe")
                .reviewRating(5)
                .build();
        Review mockReview = new Review(1, new ReviewIdentifier(), "2123eas", "John Doe", LocalDate.now(), "This is a review", 3, true);

        // Mock behavior
        when(reviewRepository.findReviewByReviewId_ReviewIdAndUserId(reviewId, userId)).thenReturn(mockReview);

        // Act and Assert
        assertThrows(ReviewBadRequestException.class, () -> reviewServiceImpl.updateCustomerReview(reviewId, userId, reviewUpdateRequest));

    }

}






