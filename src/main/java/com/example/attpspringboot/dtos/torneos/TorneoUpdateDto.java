package com.example.attpspringboot.dtos.torneos;

import com.example.attpspringboot.models.Torneo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Dto para actualizar torneos")
public class TorneoUpdateDto {
    @Size(min = 2, max = 30, message = "El nombre tiene que tener entre 2 y 30 caracteres")
    @Schema(description = "nombre del torneo", example = "Wimbeldon")
    String nombre;
    @Size(min = 2, max = 30, message = "La ubicacion tiene que tener entre 2 y 30 caracteres")
    @Schema(description = "ubicacion del torneo", example = "Colombia")
    String ubicacion;
    @Schema(description = "modalida del torneo", example = "INDIVIDUALES")
    Torneo.Modalidad modalidad;
    @Schema(description = "categoria del torneo", example = "MASTER_100")
    Torneo.Categoria categoria;
    @Schema(description = "superficie del torneo", example = "HIERBA")
    Torneo.Superficie superficie;
    @Min(value = 0, message = "Las vacantes no pueden ser negativas")
    @Schema(description = "vacantes del torneo", example = "30")
    Integer entradas;
    @DecimalMin(value = "0", message = "El premio no puede ser negativo")
    @Schema(description = "premio del torneo", example = "10.00")
    Double premio;
    @Schema(description = "fecha inicio del torneo", example = "2024-08-07")
    LocalDate fechaInicio;
    @Schema(description = "fecha final del torneo", example = "2024-08-09")
    LocalDate fechaFinalizacion;
    @Schema(description = "imagen del torneo", example = "https://via.placeholder.com/150")
    String imagen;
}
