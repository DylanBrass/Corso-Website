package com.corso.springboot.Review_Subdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ReviewUpdateRequest {

    String reviewId;
    boolean pinned;

}
