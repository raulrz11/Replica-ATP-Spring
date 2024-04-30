package com.example.attpspringboot.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Schema(name = "Torneos")
public class Torneo {
    public static String IMAGE_DEFAULT = "https://via.placeholder.com/150";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "identificador unico", example = "1")
    private Long id;
    @Column
    @Schema(description = "nombre del torneo", example = "Wimbeldon")
    private String nombre;
    @Column
    @Schema(description = "ubicacion del torneo", example = "Colombia")
    private String ubicacion;
    @Column
    @Schema(description = "modalida del torneo", example = "INDIVIDUALES")
    private Modalidad modalidad;
    @Column
    @Schema(description = "categoria del torneo", example = "MASTER_100")
    private Categoria categoria;
    @Column
    @Schema(description = "superficie del torneo", example = "HIERBA")
    private Superficie superficie;
    @Column
    @Schema(description = "vacantes del torneo", example = "30")
    private Integer entradas;
    @Column
    @Schema(description = "premio del torneo", example = "10.00")
    private Double premio;
    @Column
    @Schema(description = "fecha inicio del torneo", example = "2024-08-07")
    private LocalDate fechaInicio;
    @Column
    @Schema(description = "fecha final del torneo", example = "2024-08-09")
    private LocalDate fechaFinalizacion;
    @Column
    @Builder.Default
    @Schema(description = "imagen del torneo", example = "https://via.placeholder.com/150")
    private String imagen = IMAGE_DEFAULT;
    @OneToMany(mappedBy = "torneo", cascade = CascadeType.DETACH)
    @Schema(description = "lista de tenistas", example = "[Raul, Eva]")
    private List<Tenista> tenistas;

    public enum Modalidad {
        INDIVIDUALES, DOBLES, INDIVIDUALES_DOBLES;
    }

    public enum Categoria {
        MASTER_1000, MASTER_500, MASTER_250;
    }

    public enum Superficie {
        DURA, ARCILLA, HIERBA;
    }
}
