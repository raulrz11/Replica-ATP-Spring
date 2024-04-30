package com.example.attpspringboot.dtos.tenistas;

import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.models.Torneo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Schema(name = "Dto de respuesta de tenista")
public class TenistaResponse {
    @Schema(description = "nombre del tenista", example = "Raul")
    String nombre;
    @Schema(description = "ranking actual del tenista", example = "1")
    Integer ranking;
    @Schema(description = "puntos del tenista", example = "20.00")
    Double puntos;
    @Schema(description = "pais del tenista", example = "Colombia")
    String pais;
    @Schema(description = "fecha de nacimineto del tenista", example = "2024-09-08")
    LocalDate fechaNacimiento;
    @Schema(description = "edad del tenista", example = "22")
    Integer edad;
    @Schema(description = "altura del tenista", example = "69.8")
    Double altura;
    @Schema(description = "peso del tenista", example = "65.6")
    Double peso;
    @Schema(description = "inicio como profesional del tenista", example = "2024-09-08")
    LocalDate inicioProfesional;
    @Schema(description = "mano buena del tenista", example = "DERECHA")
    Tenista.Mano manoBuena;
    @Schema(description = "reves del tenista", example = "UNA_MANO")
    Tenista.Reves reves;
    @Schema(description = "entrenador del tenista", example = "Raul")
    String entrenador;
    @Schema(description = "imagen del tenista", example = "https://via.placeholder.com/150")
    String imagen;
    @Schema(description = "dinero del tenista", example = "20.00")
    Double priceMoney;
    @Schema(description = "mejor ranking del tenista", example = "2")
    Integer bestRanking;
    @Schema(description = "victroias del tenista", example = "5")
    Integer victorias;
    @Schema(description = "derrotas del tenista", example = "5")
    Integer derrotas;
    @Schema(description = "procentaje victorias y derrotas", example = "68%/54%")
    String WL;
    @Schema(description = "torneo inscrito", example = "Wimbeldon")
    String torneo;
}
