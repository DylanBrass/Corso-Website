package com.corso.springboot.Review_Subdomain.businesslayer;

import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    List<ReviewResponse> getAllReviews();

    List<ReviewResponse> getReviewsByCustomerFullName(String customerFullName);
}
