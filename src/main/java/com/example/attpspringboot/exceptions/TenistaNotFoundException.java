package com.example.attpspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TenistaNotFoundException extends RuntimeException{
    public TenistaNotFoundException(String message) {
        super(message);
    }
}
