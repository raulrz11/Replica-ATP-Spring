package com.example.attpspringboot.dtos.tenistas;

import com.example.attpspringboot.models.Tenista;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Dto para crear tenistas")
public class TenistaCreateDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 30, message = "El nombre tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El nombre no puede contener números")
    @Schema(description = "nombre del tenista", example = "Raul")
    String nombre;
    @NotNull(message = "Los puntos no pueden estar vacios")
    @DecimalMin(value = "0", message = "Los puntos no pueden ser negativos")
    @Schema(description = "puntos del tenista", example = "20.00")
    Double puntos;
    @NotBlank(message = "El pais no puede estar vacio")
    @Size(min = 2, max = 30, message = "El pais tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El pais no puede contener números")
    @Schema(description = "pais del tenista", example = "Colombia")
    String pais;
    @NotNull(message = "La fecha de nacimiento no puede estar vacia")
    @Schema(description = "fecha de nacimineto del tenista", example = "2024-09-08")
    LocalDate fechaNacimiento;
    @NotNull(message = "La altura no puede estar vacia")
    @DecimalMin(value = "0", message = "La altura no puede ser negativa")
    @Schema(description = "altura del tenista", example = "69.8")
    Double altura;
    @NotNull(message = "El peso no puede estar vacio")
    @DecimalMin(value = "0", message = "El peso no puede ser negativo")
    @Schema(description = "peso del tenista", example = "65.6")
    Double peso;
    @NotNull(message = "La fecha de inicio profesional no puede estar vacia")
    @Schema(description = "inicio como profesional del tenista", example = "2024-09-08")
    LocalDate inicioProfesional;
    @NotNull(message = "El reves no puede estar vacio")
    @Schema(description = "mano buena del tenista", example = "DERECHA")
    Tenista.Mano manoBuena;
    @NotNull(message = "El reves no puede estar vacio")
    @Schema(description = "reves del tenista", example = "UNA_MANO")
    Tenista.Reves reves;
    @NotBlank(message = "El nombre del entrenador no puede estar vacio")
    @Size(min = 2, max = 30, message = "El nombre del entrenador tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El entrenador no puede contener números")
    @Schema(description = "entrenador del tenista", example = "Raul")
    String entrenador;
    @NotNull(message = "El PriceMoney no puede estar vacio")
    @DecimalMin(value = "0", message = "El PriceMoney no puede ser negativo")
    @Schema(description = "dinero del tenista", example = "20.00")
    Double priceMoney;
    @NotNull(message = "Las victorias no pueden estar vacias")
    @Min(value = 0, message = "Las vistorias no pueden ser negativas")
    @Schema(description = "victroias del tenista", example = "5")
    Integer victorias;
    @NotNull(message = "Las victorias no pueden estar vacias")
    @Min(value = 0, message = "Las derrotas no pueden ser negativas")
    @Schema(description = "derrotas del tenista", example = "5")
    Integer derrotas;
}
