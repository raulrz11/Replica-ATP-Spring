package com.example.attpspringboot.dtos.torneos;

import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.models.Torneo;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Dto de respuesta de torneos")
public class TorneoRespose {
    @Schema(description = "nombre del torneo", example = "Wimbeldon")
    String nombre;
    @Schema(description = "ubicacion del torneo", example = "Colombia")
    String ubicacion;
    @Schema(description = "modalida del torneo", example = "INDIVIDUALES")
    Torneo.Modalidad modalidad;
    @Schema(description = "categoria del torneo", example = "MASTER_100")
    Torneo.Categoria categoria;
    @Schema(description = "superficie del torneo", example = "HIERBA")
    Torneo.Superficie superficie;
    @Schema(description = "vacantes del torneo", example = "30")
    Integer entradas;
    @Schema(description = "premio del torneo", example = "10.00")
    Double premio;
    @Schema(description = "fecha inicio del torneo", example = "2024-08-07")
    LocalDate fechaInicio;
    @Schema(description = "fecha final del torneo", example = "2024-08-09")
    LocalDate fechaFinalizacion;
    @Schema(description = "imagen del torneo", example = "https://via.placeholder.com/150")
    String imagen;
    @Schema(description = "lista de tenistas", example = "[Raul, Eva]")
    List<String> tenistas;
}
