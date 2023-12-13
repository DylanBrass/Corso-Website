package com.corso.springboot.utils;

import com.corso.springboot.configuration.security.exceptions.AddingAdminFailed;
import com.corso.springboot.utils.exceptions.CustomerNotFoundException;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import com.corso.springboot.utils.exceptions.ProfileAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalControllerHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(CustomerNotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidRequestException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(InvalidRequestException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(ProfileAlreadyExistsException ex) {
        return createHttpErrorInfo(CONFLICT, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, Exception ex) {
        final String message = ex.getMessage();

        return new HttpErrorInfo(httpStatus, message);
    }
}
