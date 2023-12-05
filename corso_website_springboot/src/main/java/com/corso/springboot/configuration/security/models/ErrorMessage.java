package com.corso.springboot.configuration.security.models;

import lombok.Generated;
import lombok.Value;

@Generated
@Value
public class ErrorMessage {
    String message;

    public static ErrorMessage from(final String message) {
        return new ErrorMessage(message);
    }
}
