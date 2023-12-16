package com.corso.springboot.configuration.security.exceptions;

import org.springframework.http.HttpStatus;

public class Auth0Error extends RuntimeException{

    public HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public Auth0Error(String message) {
        super(message);
    }

    public Auth0Error(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
