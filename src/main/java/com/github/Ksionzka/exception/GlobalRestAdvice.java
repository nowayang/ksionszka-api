package com.github.Ksionzka.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalRestAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        return ex.getBindingResult()
            .getAllErrors()
            .stream()
            .findFirst()
            .map(objectError -> {
                String message = objectError.getDefaultMessage();
                return this.handleRestException(RestException.of(HttpStatus.BAD_REQUEST, message));
            })
            .orElseGet(() -> this.handleRestException(RestException.of(HttpStatus.BAD_REQUEST, ErrorMessage.BAD_REQUEST)));
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Object> handleRestException(RestException restException) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        return new ResponseEntity<>(
            ApiError.builder()
                .status(restException.getHttpStatus())
                .message(restException.getMessage())
                .build(),
            httpHeaders,
            restException.getHttpStatus()
        );
    }
}
