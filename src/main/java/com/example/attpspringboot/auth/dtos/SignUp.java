package com.example.attpspringboot.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
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
    @NotBlank(message = "La contraseña no puede estar vacia")
    @Size(min = 8, max = 20, message = "La contraseña tiene que tener entre 8 y 20 caracteres")
    @Pattern.List({
            @Pattern(regexp = ".*[a-z].*", message = "La contraseña debe contener al menos una letra minúscula"),
            @Pattern(regexp = ".*[A-Z].*", message = "La contraseña debe contener al menos una letra mayúscula"),
            @Pattern(regexp = ".*\\d.*", message = "La contraseña debe contener al menos un dígito"),
    })
    String password;
    @NotBlank(message = "La contraseña de comprobacion no puede estar vacia")
    String passwordComprobation;
}
