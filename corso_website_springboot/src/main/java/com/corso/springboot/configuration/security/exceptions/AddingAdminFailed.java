package com.corso.springboot.configuration.security.exceptions;

import org.springframework.http.HttpStatus;

public class AddingAdminFailed extends RuntimeException{

    public HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public AddingAdminFailed(String message) {
        super(message);
    }

    public AddingAdminFailed(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
