package com.corso.springboot.Review_Subdomain.businesslayer;

import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewUpdateRequest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReviewService {

    List<ReviewResponse> getAllReviews(Map<String, String> queryParams, int pageSize, int offset);

    List<ReviewResponse> getReviewsByCustomerFullName(String customerFullName);

    List<ReviewResponse> getPinnedReviews(boolean pinned);

    List<ReviewResponse> getReviewsByUserId(String userId);
    ReviewResponse getReviewByReviewId(String reviewId);
    ReviewResponse updatePinnedReviewByReviewId(String reviewId, ReviewUpdateRequest reviewRequest) throws MessagingException, IOException, InterruptedException;

    ReviewResponse customerCreateReview(String userId, ReviewRequest reviewRequest);

    Integer getCountAllByState(boolean state);

    Integer getCountAllReviews();
}
