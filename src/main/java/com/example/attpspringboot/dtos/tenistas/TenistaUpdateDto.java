package com.example.attpspringboot.dtos.tenistas;

import com.example.attpspringboot.models.Tenista;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Dto para actualizar tenistas")
public class TenistaUpdateDto {
    @Size(min = 2, max = 30, message = "El nombre tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El nombre no puede contener números")
    @Schema(description = "nombre del tenista", example = "Raul")
    String nombre;
    @Schema(description = "ranking actual del tenista", example = "1")
    Integer ranking;
    @DecimalMin(value = "0", message = "Los puntos no pueden ser negativos")
    @Schema(description = "puntos del tenista", example = "20.00")
    Double puntos;
    @Size(min = 2, max = 30, message = "El pais tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El pais no puede contener números")
    @Schema(description = "pais del tenista", example = "Colombia")
    String pais;
    @Schema(description = "fecha de nacimineto del tenista", example = "2024-09-08")
    LocalDate fechaNacimiento;
    @Schema(description = "edad del tenista", example = "22")
    Integer edad;
    @DecimalMin(value = "0", message = "La altura no puede ser negativa")
    @Schema(description = "altura del tenista", example = "69.8")
    Double altura;
    @DecimalMin(value = "0", message = "La altura no puede ser negativa")
    @Schema(description = "peso del tenista", example = "65.6")
    Double peso;
    @Schema(description = "inicio como profesional del tenista", example = "2024-09-08")
    LocalDate inicioProfesional;
    @Schema(description = "mano buena del tenista", example = "DERECHA")
    Tenista.Mano manoBuena;
    @Schema(description = "reves del tenista", example = "UNA_MANO")
    Tenista.Reves reves;
    @Size(min = 2, max = 30, message = "El nombre del entrnador tiene que tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "El entrenador no puede contener números")
    @Schema(description = "entrenador del tenista", example = "Raul")
    String entrenador;
    @Schema(description = "imagen del tenista", example = "https://via.placeholder.com/150")
    String imagen;
    @DecimalMin(value = "0", message = "El PriceMoney no puede ser negativo")
    @Schema(description = "dinero del tenista", example = "20.00")
    Double priceMoney;
    @Schema(description = "mejor ranking del tenista", example = "2")
    Integer bestRanking;
    @Min(value = 0, message = "Las victorias no pueden ser negativas")
    @Schema(description = "victroias del tenista", example = "5")
    Integer victorias;
    @Min(value = 0, message = "Las derrotas no pueden ser negativas")
    @Schema(description = "derrotas del tenista", example = "5")
    Integer derrotas;
    @Schema(description = "procentaje victorias y derrotas", example = "68%/54%")
    String WL;
    @Schema(description = "torneo inscrito", example = "Wimbeldon")
    String torneo;
}
