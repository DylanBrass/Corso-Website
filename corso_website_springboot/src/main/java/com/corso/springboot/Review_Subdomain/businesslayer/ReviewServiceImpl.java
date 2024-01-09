package com.corso.springboot.Review_Subdomain.businesslayer;


import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{


    private final ReviewRepository reviewRepository;

    private final ReviewResponseMapper reviewResponseMapper;
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

}
