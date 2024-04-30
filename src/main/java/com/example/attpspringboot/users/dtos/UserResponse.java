package com.example.attpspringboot.users.dtos;

import com.example.attpspringboot.users.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    Long id;
    String nombre;
    String username;
    String email;
    String password;
    Set<User.Rol> rols;
}
