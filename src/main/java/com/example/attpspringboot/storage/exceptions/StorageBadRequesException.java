package com.example.attpspringboot.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequesException extends RuntimeException{
    public StorageBadRequesException(String message) {
        super(message);
    }
}
