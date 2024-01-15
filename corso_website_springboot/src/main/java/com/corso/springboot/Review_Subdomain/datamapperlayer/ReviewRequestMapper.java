package com.corso.springboot.Review_Subdomain.datamapperlayer;


import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewIdentifier;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewRequestMapper {

    Review toReviewPinnedRequest(ReviewUpdateRequest reviewUpdateRequest);

    ReviewIdentifier map(String value);

    Review toReview(ReviewRequest reviewRequest);
}
