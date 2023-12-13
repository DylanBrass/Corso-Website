package com.corso.springboot.utils.exceptions;

public class ProfileAlreadyExistsException extends RuntimeException{

            public ProfileAlreadyExistsException(String message) {
                super(message);
            }
}
