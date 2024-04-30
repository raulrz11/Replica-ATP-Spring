package com.example.attpspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TenistaBadRequestException extends RuntimeException{
    public TenistaBadRequestException(String message) {
        super(message);
    }
}
