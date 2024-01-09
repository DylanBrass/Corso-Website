package com.corso.springboot.Review_Subdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewRequest {



    public String userId;

    public String customerFullName;

    public LocalDate reviewDate;

    public String message;

    public int reviewRating;

    public boolean pinned;
}
