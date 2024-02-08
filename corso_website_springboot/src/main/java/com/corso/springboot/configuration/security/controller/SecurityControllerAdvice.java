package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.configuration.security.exceptions.AddingAdminFailed;
import com.corso.springboot.configuration.security.exceptions.Auth0Error;
import com.corso.springboot.utils.HttpErrorInfo;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Generated
@RestControllerAdvice
public class SecurityControllerAdvice {
    @ExceptionHandler(AddingAdminFailed.class)
    public ResponseEntity<HttpErrorInfo> handleErrorWhileCreatingAdmin(AddingAdminFailed ex) {
        createHttpErrorInfo(ex.httpStatus, ex);

        return ResponseEntity
                .status(ex.httpStatus)
                .body(createHttpErrorInfo(ex.httpStatus, ex));

    }

    @ExceptionHandler(Auth0Error.class)
    public ResponseEntity<HttpErrorInfo> handleErrorWhileCreatingAdmin(Auth0Error ex) {
        createHttpErrorInfo(ex.httpStatus, ex);

        return ResponseEntity
                .status(ex.httpStatus)
                .body(createHttpErrorInfo(ex.httpStatus, ex));

    }



    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, Exception ex) {
        final String message = ex.getMessage();

        return new HttpErrorInfo(httpStatus, message);
    }

}

