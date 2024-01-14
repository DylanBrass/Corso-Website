package com.corso.springboot.Review_Subdomain.businesslayer;


import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewIdentifier;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewRequestMapper;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import com.corso.springboot.utils.exceptions.Review.ReviewBadRequestException;
import com.corso.springboot.utils.exceptions.Review.ReviewUserHasNoOrdersException;
import com.corso.springboot.utils.exceptions.Review.ReviewsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewResponseMapper.toReviewsResponse(reviewRepository.findAll());
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
