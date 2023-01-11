package com.github.Ksionzka.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class ApiError {
    private final HttpStatus status;
    private final String message;

    @Builder.Default
    private final ZonedDateTime timestamp = ZonedDateTime.now();

    @Builder
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
