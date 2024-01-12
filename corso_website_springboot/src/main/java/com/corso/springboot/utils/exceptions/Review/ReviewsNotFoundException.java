package com.corso.springboot.utils.exceptions.Review;

public class ReviewsNotFoundException extends RuntimeException {

    public ReviewsNotFoundException(String message) {
        super(message);
    }
}
