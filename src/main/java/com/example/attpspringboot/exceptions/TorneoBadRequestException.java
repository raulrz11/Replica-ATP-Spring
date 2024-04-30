package com.example.attpspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TorneoBadRequestException extends RuntimeException{
    public TorneoBadRequestException(String message) {
        super(message);
    }
}
