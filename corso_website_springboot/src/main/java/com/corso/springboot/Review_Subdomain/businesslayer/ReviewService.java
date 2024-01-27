package com.corso.springboot.Review_Subdomain.businesslayer;

import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewUpdateRequest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReviewService {

    List<ReviewResponse> getReviews(Map<String, String> params, int pageSize, int offset);

    List<ReviewResponse> getPinnedReviews(boolean pinned);

    List<ReviewResponse> getReviewsByUserId(String userId);
    ReviewResponse getReviewByReviewId(String reviewId);
    ReviewResponse updatePinnedReviewByReviewId(String reviewId, ReviewUpdateRequest reviewRequest) throws MessagingException, IOException, InterruptedException;

    ReviewResponse customerCreateReview(String userId, ReviewRequest reviewRequest);

    Integer getCountAllByState(boolean state);

    Integer getCountAllReviews();

    ReviewResponse getReviewByReviewIdAndUserId(String reviewId, String userId);

    void deleteCustomerReview(String reviewId, String userId) throws MessagingException;

    ReviewResponse updateCustomerReview(String reviewId, String userId, ReviewRequest reviewUpdateRequest);
}
