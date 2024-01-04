package com.corso.springboot.Review_Subdomain.datamapperlayer;


import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewRequestMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "reviewId")
    @Mapping(ignore = true, target = "userId")
    @Mapping(ignore = true, target = "customerFullName")
    @Mapping(ignore = true, target = "message")
    @Mapping(ignore = true, target = "reviewRating")
    Review requestModelToReview(ReviewRequest reviewRequest);
}
