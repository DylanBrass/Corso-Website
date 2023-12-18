package com.corso.springboot.configuration.security.exceptions;

import lombok.Generated;
import org.springframework.http.HttpStatus;

@Generated
public class Auth0Error extends RuntimeException{

    public HttpStatus httpStatus;


    public Auth0Error(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
