package com.example.attpspringboot.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PassworNotMatchException extends RuntimeException{
    public PassworNotMatchException(String message) {
        super(message);
    }
}
