package com.corso.springboot.utils.exceptions.Review;

public class ReviewUserHasNoOrdersException extends RuntimeException{

    public ReviewUserHasNoOrdersException(String message) {
        super(message);
    }
}
