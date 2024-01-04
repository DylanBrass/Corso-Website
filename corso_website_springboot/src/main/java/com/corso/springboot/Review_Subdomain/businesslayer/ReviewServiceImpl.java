package com.corso.springboot.Review_Subdomain.businesslayer;


import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.Review_Subdomain.datamapperlayer.ReviewResponseMapper;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{


    private final ReviewRepository reviewRepository;

    private final ReviewResponseMapper reviewResponseMapper;
    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewResponseMapper.toReviewsResponse(reviewRepository.findAll());
    }
}
