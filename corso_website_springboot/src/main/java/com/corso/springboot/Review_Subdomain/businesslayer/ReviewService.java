package com.corso.springboot.Review_Subdomain.businesslayer;

import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getAllReviews();
}
