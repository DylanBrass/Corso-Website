package com.corso.springboot.utils;

import com.corso.springboot.utils.exceptions.CustomerNotFoundException;
import com.corso.springboot.utils.exceptions.FAQ.FAQNotFoundException;
import com.corso.springboot.utils.exceptions.FAQ.ThreePrefferedFaqsException;
import com.corso.springboot.utils.exceptions.Gallery.GalleryNotFoundException;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import com.corso.springboot.utils.exceptions.Orders.OrderNotFoundException;
import com.corso.springboot.utils.exceptions.Review.*;
import com.corso.springboot.utils.exceptions.services.ServiceBadRequestException;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalControllerHandler {


    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IllegalArgumentException.class)
    public HttpErrorInfo handleIllegalArgumentException(IllegalArgumentException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public HttpErrorInfo handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return createHttpErrorInfo(BAD_REQUEST, ex);
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ServiceBadRequestException.class)
    public HttpErrorInfo handleErrorWhileCreatingReport(ServiceBadRequestException ex) {
        return createHttpErrorInfo(BAD_REQUEST, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(IOException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(IOException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex);
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ReviewUserHasNoOrdersException.class)
    public HttpErrorInfo handleErrorUserHasNoOrdersForReview(ReviewUserHasNoOrdersException ex) {
        return createHttpErrorInfo(BAD_REQUEST, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ReviewPinnedLimitReachedException.class)
    public HttpErrorInfo handleErrorPinnedLimitReached(ReviewPinnedLimitReachedException ex) {
        return createHttpErrorInfo(CONFLICT, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ReviewBadRequestException.class)
    public HttpErrorInfo handleErrorWhileCreatingReview(ReviewBadRequestException ex) {
        return createHttpErrorInfo(BAD_REQUEST, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ReviewStateConflictException.class)
    public HttpErrorInfo handleErrorWhileUpdatingReviewStateConflict(ReviewStateConflictException ex) {
        return createHttpErrorInfo(CONFLICT, ex);
    }


    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(CustomerNotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, ex);
    }

    @Generated
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidRequestException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(InvalidRequestException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex);
    }

    @Generated
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MessagingException.class)
    public HttpErrorInfo handleErrorWhileSendingEmail(MessagingException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex);
    }


    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(OrderNotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(GalleryNotFoundException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(GalleryNotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ReviewsNotFoundException.class)
    public HttpErrorInfo handleErrorWhileGettingReviews(ReviewsNotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(FAQNotFoundException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(FAQNotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ThreePrefferedFaqsException.class)
    public HttpErrorInfo handleErrorWhileCreatingAdmin(ThreePrefferedFaqsException ex) {
        return createHttpErrorInfo(CONFLICT, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, Exception ex) {
        final String message = ex.getMessage();

        return new HttpErrorInfo(httpStatus, message);
    }
}
