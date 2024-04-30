package com.example.attpspringboot.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Schema(name = "Tenistas")
public class Tenista {
    public static String IMAGE_DEFAULT = "https://via.placeholder.com/150";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "identificador unico", example = "1")
    private Long id;
    @Column
    @Schema(description = "nombre del tenista", example = "Raul")
    private String nombre;
    @Column
    @Schema(description = "ranking actual del tenista", example = "1")
    private Integer ranking;
    @Column
    @Schema(description = "puntos del tenista", example = "20.00")
    private Double puntos;
    @Column
    @Schema(description = "pais del tenista", example = "Colombia")
    private String pais;
    @Column
    @Schema(description = "fecha de nacimineto del tenista", example = "2024-09-08")
    private LocalDate fechaNacimiento;
    @Column
    @Schema(description = "edad del tenista", example = "22")
    private Integer edad;
    @Column
    @Schema(description = "altura del tenista", example = "69.8")
    private Double altura;
    @Column
    @Schema(description = "peso del tenista", example = "65.6")
    private Double peso;
    @Column
    @Schema(description = "inicio como profesional del tenista", example = "2024-09-08")
    private LocalDate inicioProfesional;
    @Column
    @Schema(description = "mano buena del tenista", example = "DERECHA")
    private Mano manoBuena;
    @Column
    @Schema(description = "reves del tenista", example = "UNA_MANO")
    private Reves reves;
    @Column
    @Schema(description = "entrenador del tenista", example = "Raul")
    private String entrenador;
    @Column
    @Schema(description = "imagen del tenista", example = "https://via.placeholder.com/150")
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
    @Column
    @Schema(description = "dinero del tenista", example = "20.00")
    private Double priceMoney;
    @Column
    @Schema(description = "mejor ranking del tenista", example = "2")
    private Integer bestRanking;
    @Column
    @Schema(description = "victroias del tenista", example = "5")
    private Integer victorias;
    @Column
    @Schema(description = "derrotas del tenista", example = "5")
    private Integer derrotas;
    @Column
    @Schema(description = "procentaje victorias y derrotas", example = "68%/54%")
    private String WL;
    @ManyToOne
    @JoinColumn(name = "torneo_id")
    @Schema(description = "torneo inscrito", example = "Wimbeldon")
    private Torneo torneo;

    public enum Mano {
        DERECHA, IZQUIERDA;
    }

    public enum Reves {
        UNA_MANO, DOS_MANOS;
    }
}

