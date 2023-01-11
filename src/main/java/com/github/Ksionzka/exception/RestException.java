package com.github.Ksionzka.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private final HttpStatus httpStatus;

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static RestException of(HttpStatus httpStatus, String message) {
        return new RestException(message, httpStatus);
    }

    public static RestException of(HttpStatus httpStatus, ErrorMessage message) {
        return of(httpStatus, message.readableString());
    }
}
