package com.corso.springboot.Review_Subdomain.datamapperlayer;


import com.corso.springboot.Review_Subdomain.datalayer.Review;
import com.corso.springboot.Review_Subdomain.presentationlayer.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewRequestMapper {

    @Mapping(target = "reviewId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewDate", ignore = true)
    @Mapping(target = "pinned", ignore = true)
    Review toReview(ReviewRequest reviewRequest);
}
