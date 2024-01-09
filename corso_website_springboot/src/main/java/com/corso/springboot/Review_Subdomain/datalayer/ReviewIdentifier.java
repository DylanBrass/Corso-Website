package com.corso.springboot.Review_Subdomain.datalayer;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ReviewIdentifier {

    @Column(name = "review_id")
    private String reviewId;

    public ReviewIdentifier() {
        this.reviewId = java.util.UUID.randomUUID().toString();
    }

    public String getReviewId() {
        return reviewId;
    }
}
