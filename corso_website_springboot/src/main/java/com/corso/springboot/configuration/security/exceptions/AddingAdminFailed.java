package com.corso.springboot.configuration.security.exceptions;

import lombok.Generated;
import org.springframework.http.HttpStatus;

@Generated
public class AddingAdminFailed extends RuntimeException{

    public HttpStatus httpStatus;
    public AddingAdminFailed(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
