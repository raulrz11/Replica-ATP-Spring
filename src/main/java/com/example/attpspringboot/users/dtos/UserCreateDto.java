package com.example.attpspringboot.users.dtos;

import com.example.attpspringboot.users.models.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 30, message = "El nombre tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El nombre no puede contener números")
    String nombre;
    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    @Size(min = 2, max = 30, message = "El nombre de usuario tiene que tener entre 2 y 30 caracteres")
    String username;
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Email no valido. Ej: xxxx@xxxx.com")
    String email;
    @NotBlank(message = "El contraseña no puede estar vacio")
    @Size(min = 8, max = 20, message = "El contraseña tiene que tener entre 8 y 20 caracteres")
    @Pattern.List({
            @Pattern(regexp = ".*[a-z].*", message = "La contraseña debe contener al menos una letra minúscula"),
            @Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener al menos una letra mayúscula"),
            @Pattern(regexp = ".*\\d.*", message = "La contraseña debe contener al menos un dígito"),
    })
    String password;
    @NotNull(message = "Los roles no pueden estar vacios")
    Set<User.Rol> rols;
}
