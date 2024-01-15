package com.corso.springboot.utils.exceptions.Review;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(String reviewId) {
        super("Review with id " + reviewId + " not found");
    }
}
