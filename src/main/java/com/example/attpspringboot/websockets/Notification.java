package com.example.attpspringboot.websockets;

import java.time.LocalDateTime;

public record Notification<T> (
        String entity,
        Tipo type,
        T data,
        String createdAt
){
    public enum Tipo{
        CREATE, UPDATE, DELETE
    }
}
