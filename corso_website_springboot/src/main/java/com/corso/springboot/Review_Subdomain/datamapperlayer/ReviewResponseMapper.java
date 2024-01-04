package com.corso.springboot.Review_Subdomain.datamapperlayer;



import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewResponseMapper {

    @Mapping(expression="java(review.getReviewId().getReviewId())", target = "reviewId")
    //@Mapping(expression="java(review.getUserId().getUserId())", target = "userId")
    //@Mapping(expression="java(review.getCustomerFullName().getCustomerFullName())", target = "customerFullName")
    //@Mapping(expression="java(review.getReviewDate().getReviewDate())", target = "reviewDate")

    //get 1 review
    ReviewResponse toReviewResponse(Review review);

    //get all reviews
    List<ReviewResponse> toReviewsResponse(List<Review> reviews);
}
