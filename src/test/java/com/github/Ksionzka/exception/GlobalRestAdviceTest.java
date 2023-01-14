package com.github.Ksionzka.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class GlobalRestAdviceTest {
    /**
     * Method under test: {@link GlobalRestAdvice#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid() {
        GlobalRestAdvice globalRestAdvice = new GlobalRestAdvice();
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null,
                new BindException("Target", "Object Name"));

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = globalRestAdvice.handleMethodArgumentNotValid(ex,
                headers, HttpStatus.CONTINUE, new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals("bad request", ((ApiError) actualHandleMethodArgumentNotValidResult.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ((ApiError) actualHandleMethodArgumentNotValidResult.getBody()).getStatus());
    }


    /**
     * Method under test: {@link GlobalRestAdvice#handleRestException(RestException)}
     */
    @Test
    void testHandleRestException() {
        GlobalRestAdvice globalRestAdvice = new GlobalRestAdvice();
        ResponseEntity<Object> actualHandleRestExceptionResult = globalRestAdvice
                .handleRestException(RestException.of(HttpStatus.CONTINUE, "An error occurred"));
        assertTrue(actualHandleRestExceptionResult.hasBody());
        assertEquals(1, actualHandleRestExceptionResult.getHeaders().size());
        assertEquals(HttpStatus.CONTINUE, actualHandleRestExceptionResult.getStatusCode());
        assertEquals("An error occurred", ((ApiError) actualHandleRestExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.CONTINUE, ((ApiError) actualHandleRestExceptionResult.getBody()).getStatus());
    }

}

