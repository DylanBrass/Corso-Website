package com.corso.springboot.utils.exceptions.Review;

public class ReviewPinnedLimitReachedException extends RuntimeException{

        public ReviewPinnedLimitReachedException(String message) {
            super(message);
        }
}
