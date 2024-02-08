package com.corso.springboot.utils.exceptions.services;

public class ServiceBadRequestException extends RuntimeException {
    public ServiceBadRequestException(String message) {
        super(message);
    }
}
