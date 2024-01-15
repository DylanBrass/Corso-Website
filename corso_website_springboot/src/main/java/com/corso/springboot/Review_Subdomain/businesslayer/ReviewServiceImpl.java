package com.corso.springboot.Review_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewIdentifier;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewRequestMapper;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.exceptions.CustomerNotFoundException;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import com.corso.springboot.utils.exceptions.Review.*;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final OrderService orderService;

    private final ReviewRepository reviewRepository;

    private final ReviewResponseMapper reviewResponseMapper;

    private final ReviewRequestMapper reviewRequestMapper;

    private final CustomerService customerService;

    private final Auth0ManagementService auth0ManagementService;

    private final EmailService emailService;

    @Override
    public List<ReviewResponse> getAllReviews(Map<String, String> queryParams, int pageSize, int offset) {
        return reviewResponseMapper.toReviewsResponse(reviewRepository.findAllReviews(pageSize, offset));
    }

    @Override
    public List<ReviewResponse> getReviewsByCustomerFullName(String customerFullName) {
        if (customerFullName != null && !customerFullName.isEmpty()) {
            return reviewResponseMapper.toReviewsResponse(reviewRepository.findAllByCustomerFullNameStartingWith(customerFullName));
        } else {
            return reviewResponseMapper.toReviewsResponse(reviewRepository.findAll());
        }
    }

    @Override
    public List<ReviewResponse> getPinnedReviews(boolean pinned) {
        return reviewResponseMapper.toReviewsResponse(reviewRepository.getReviewByPinned(pinned));

    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(String userId) {
        List<ReviewResponse> reviews = reviewResponseMapper.toReviewsResponse(reviewRepository.findAllByUserId(userId));

        if (reviews.isEmpty()) {
            throw new ReviewsNotFoundException("No reviews were found for this user");
        }

        return reviews;
    }

    public ReviewResponse getReviewByReviewId(String reviewId) {
        return reviewResponseMapper.toReviewResponse(reviewRepository.findReviewByReviewId_ReviewId(reviewId));
    }

    public ReviewResponse updatePinnedReviewByReviewId(String reviewId, ReviewUpdateRequest reviewRequest) throws MessagingException, IOException, InterruptedException {

        Review review = reviewRepository.findReviewByReviewId_ReviewId(reviewId);

        if (review == null) {
            throw new ReviewNotFoundException("Review not found");
        }

        if (reviewRequest.isPinned() == review.isPinned()) {
            throw new ReviewStateConflictException("Review state is the same");
        }


        if (reviewRequest.isPinned()) {
            int pinnedReviews = reviewRepository.countAllByPinned(true);
            // Add 1 because the review is not yet pinned
            if (pinnedReviews + 1 > 6) {
                throw new ReviewPinnedLimitReachedException("Pinned reviews limit reached");
            }
        }

        review.setPinned(reviewRequest.isPinned());


        ReviewResponse reviewSaved = reviewResponseMapper.toReviewResponse(reviewRepository.save(review));

        if (reviewSaved == null) {
            throw new InvalidRequestException("Review not found");
        }

        //send email to user
        String email;

        try {
            email = customerService.getCustomerByUserId(reviewSaved.getUserId()).getEmail();
        }catch (CustomerNotFoundException e) {
            log.error("Customer not found");
            email = auth0ManagementService.getUserInfo(reviewSaved.getUserId()).getEmail();
        }

        if (email != null && !email.isEmpty()) {
            HashMap<String, String> emailParams = new HashMap<>();

            emailParams.put("customerFullName", reviewSaved.getCustomerFullName());

            int emailStatus;
            if (reviewSaved.isPinned()) {
                emailParams.put("message", reviewSaved.getReviewId());
               emailStatus = emailService.sendEmail(email, "Review pinned", "ReviewPinnedChangeEmail", emailParams);

            } else {
                emailParams.put("message", reviewSaved.getReviewId());
                emailStatus = emailService.sendEmail(email, "Review unpinned", "ReviewPinnedChangeEmail", emailParams);
            }

            if (emailStatus != 200) {
                log.error("Email not sent");
                throw new MessagingException("Email not sent");
            }
        }


        return reviewSaved;

    }


    @Override
    public Integer getCountAllByState(boolean state) {
        return reviewRepository.countAllByPinned(state);
    }

    @Override
    public Integer getCountAllReviews() {
        return reviewRepository.countAllBy();
    }

    @Override
    public ReviewResponse customerCreateReview(String userId, ReviewRequest reviewRequest) {

        if (!orderService.findAnyUserOrders(userId)) {
            throw new ReviewUserHasNoOrdersException("No orders attributed to account, please place an order before leaving a review. If you want to voice a concern, please contact us, this measure is in place to prevent spam reviews, we still want to hear from you!");
        }

        if (reviewRepository.reviewInLastWeekForUser(userId)) {
            throw new ReviewBadRequestException("You have already left a review this week!");
        }

        if (reviewRequest.getReviewRating() < 1 || reviewRequest.getReviewRating() > 5) {
            throw new ReviewBadRequestException("Review rating must be between 1 and 5");
        }


        Review review = reviewRequestMapper.toReview(reviewRequest);

        review.setReviewId(new ReviewIdentifier());
        review.setUserId(userId);
        review.setReviewDate(LocalDate.now());
        review.setPinned(false);

        log.info("Review: {}", review);
        log.info("Review Date: {}", review.getReviewDate());

        return reviewResponseMapper.toReviewResponse(reviewRepository.save(review));

    }












}
