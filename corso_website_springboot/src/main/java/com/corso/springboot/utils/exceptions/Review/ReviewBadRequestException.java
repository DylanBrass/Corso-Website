package com.corso.springboot.utils.exceptions.Review;

public class ReviewBadRequestException extends RuntimeException{
    public ReviewBadRequestException(String message) {
        super(message);
    }
}
