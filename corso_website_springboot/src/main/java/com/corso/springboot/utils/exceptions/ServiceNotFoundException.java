package com.corso.springboot.utils.exceptions;

public class ServiceNotFoundException extends RuntimeException{

        public ServiceNotFoundException(String message) {
            super(message);
        }
}
