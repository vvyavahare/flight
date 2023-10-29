package com.hackerrank.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FlightExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResponseNotFoundException.class)
    public ResponseEntity<Object> handleResouseNotFoundException(ResponseNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
