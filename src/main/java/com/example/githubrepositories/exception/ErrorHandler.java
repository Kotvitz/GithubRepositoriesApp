package com.example.githubrepositories.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.githubrepositories.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {
	
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResponseStatusException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found");
    }
    
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleNotAcceptable() {
        return new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Not Acceptable - Please use JSON format");
    }
}
