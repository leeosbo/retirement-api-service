package com.retirement.apiservice.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InvalidInputExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest) {

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(LocalDateTime.now(),
                exception.getFieldError().getField(),
                exception.getFieldError().getDefaultMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(LocalDateTime.now(),
                "Invalid Input Type",
                exception.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<Object>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
