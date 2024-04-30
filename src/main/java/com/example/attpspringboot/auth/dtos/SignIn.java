package com.example.attpspringboot.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignIn {
    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    String username;
    @NotBlank(message = "La contraseña de usuario no puede estar vacia")
    String password;
}
