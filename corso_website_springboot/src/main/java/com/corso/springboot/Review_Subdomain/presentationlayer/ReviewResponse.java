package com.corso.springboot.Review_Subdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;


@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class ReviewResponse {


    public String reviewId;

    public String userId;

    public String customerFullName;

    public LocalDate reviewDate;

    public String message;

    public int reviewRating;

    public boolean pinned;

    public int totalReviewsMatchingRequest;


}
