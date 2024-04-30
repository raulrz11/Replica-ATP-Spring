package com.example.attpspringboot.dtos.torneos;

import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.models.Torneo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Dto para crear Torneos")
public class TorneoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 30, message = "El nombre tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El entrenador no puede contener números")
    @Schema(description = "nombre del torneo", example = "Wimbeldon")
    String nombre;
    @NotBlank(message = "La ubicacion no puede estar vacia")
    @Size(min = 2, max = 30, message = "La ubicacion tiene que tener entre 2 y 30 caracteres")
    ///CAMBIARLO -> A que se pueda poner string con numeros ///
    @Pattern(regexp = "^[^0-9]*$", message = "El entrenador no puede contener números")
    //////////////////////////////////////////////////////
    @Schema(description = "ubicacion del torneo", example = "Colombia")
    String ubicacion;
    @NotNull(message = "La modalidad no puede estar vacia")
    @Schema(description = "modalida del torneo", example = "INDIVIDUALES")
    Torneo.Modalidad modalidad;
    @NotNull(message = "La categoria no puede estar vacia")
    @Schema(description = "categoria del torneo", example = "MASTER_100")
    Torneo.Categoria categoria;
    @NotNull(message = "La superficie no puede estar vacia")
    @Schema(description = "superficie del torneo", example = "HIERBA")
    Torneo.Superficie superficie;
    @NotNull(message = "Las vacantes no pueden estar vacias")
    @Min(value = 0, message = "Las vacantes no pueden ser negativas")
    @Schema(description = "vacantes del torneo", example = "30")
    Integer entradas;
    @NotNull(message = "El premio no puede estar vacio")
    @DecimalMin(value = "0", message = "El premio no puede ser negativo")
    @Schema(description = "premio del torneo", example = "10.00")
    Double premio;
    @NotNull(message = "La fecha de inicio no puede estar vacia")
    @Future(message = "La fecha de inicio debe ser posterior a la fecha actual")
    @Schema(description = "fecha inicio del torneo", example = "2024-08-07")
    LocalDate fechaInicio;
    @NotNull(message = "La fecha de finalizacion no puede estar vacia")
    @Future(message = "La fecha de finalizacion debe ser posterior a la fecha actual")
    @Schema(description = "fecha final del torneo", example = "2024-08-09")
    LocalDate fechaFinalizacion;
    @Schema(description = "lista de tenistas", example = "[Raul, Eva]")
    List<Tenista> tenistas;
}
