package com.corso.springboot.utils.exceptions.FAQ;

public class FAQNotFoundException extends RuntimeException {
    public FAQNotFoundException(String message) {
        super(message);
    }
}
