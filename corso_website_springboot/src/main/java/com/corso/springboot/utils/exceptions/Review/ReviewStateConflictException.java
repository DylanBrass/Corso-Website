package com.corso.springboot.utils.exceptions.Review;

public class ReviewStateConflictException extends RuntimeException{


    public ReviewStateConflictException(String message) {
        super(message);
    }
}
