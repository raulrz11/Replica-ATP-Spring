package com.example.attpspringboot.mappers;

import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.models.Torneo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TorneosMapperTest {
    private final TorneosMapper mapper = new TorneosMapper();

    @Test
    void toEntity() {
        TorneoCreateDto dto = TorneoCreateDto.builder()
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .tenistas(new ArrayList<>())
                .build();

        var res = mapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getNombre(), res.getNombre()),
                () -> assertEquals(dto.getUbicacion(), res.getUbicacion()),
                () -> assertEquals(dto.getModalidad(), res.getModalidad()),
                () -> assertEquals(dto.getCategoria(), res.getCategoria()),
                () -> assertEquals(dto.getSuperficie(), res.getSuperficie()),
                () -> assertEquals(dto.getEntradas(), res.getEntradas()),
                () -> assertEquals(dto.getPremio(), res.getPremio()),
                () -> assertEquals(dto.getFechaInicio(), res.getFechaInicio()),
                () -> assertEquals(dto.getFechaFinalizacion(), res.getFechaFinalizacion()),
                () -> assertEquals(dto.getTenistas(), res.getTenistas())
        );
    }

    @Test
    void testToEntity() {
        TorneoUpdateDto dto = TorneoUpdateDto.builder()
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .imagen("http://placeimg.com/640/480/people")
                .build();

        Torneo existingTorneo = Torneo.builder()
                .id(1L)
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .imagen("http://placeimg.com/640/480/people")
                .tenistas(new ArrayList<>())
                .build();

        var res = mapper.toEntity(dto, existingTorneo);

        assertAll(
                () -> assertEquals(dto.getNombre(), existingTorneo.getNombre()),
                () -> assertEquals(dto.getUbicacion(), existingTorneo.getUbicacion()),
                () -> assertEquals(dto.getModalidad(), existingTorneo.getModalidad()),
                () -> assertEquals(dto.getCategoria(), existingTorneo.getCategoria()),
                () -> assertEquals(dto.getSuperficie(), existingTorneo.getSuperficie()),
                () -> assertEquals(dto.getEntradas(), existingTorneo.getEntradas()),
                () -> assertEquals(dto.getPremio(), existingTorneo.getPremio()),
                () -> assertEquals(dto.getImagen(), existingTorneo.getImagen()),
                () -> assertEquals(dto.getFechaInicio(), existingTorneo.getFechaInicio()),
                () -> assertEquals(dto.getFechaFinalizacion(), existingTorneo.getFechaFinalizacion()),
                () -> assertEquals(dto.getImagen(), existingTorneo.getImagen())
        );
    }

    @Test
    void toDto() {
        Torneo existingTorneo = Torneo.builder()
                .id(1L)
                .nombre("Wimbledon")
                .ubicacion("Londres, Reino Unido")
                .modalidad(Torneo.Modalidad.INDIVIDUALES)
                .categoria(Torneo.Categoria.MASTER_1000)
                .superficie(Torneo.Superficie.HIERBA)
                .entradas(16)
                .premio(2000000.00)
                .fechaInicio(LocalDate.of(2024, 6, 23))
                .fechaFinalizacion(LocalDate.of(2024, 7, 6))
                .imagen("http://placeimg.com/640/480/people")
                .tenistas(new ArrayList<>())
                .build();

        var res =  mapper.toDto(existingTorneo);

        assertAll(
                () -> assertEquals(existingTorneo.getNombre(), res.getNombre()),
                () -> assertEquals(existingTorneo.getUbicacion(), res.getUbicacion()),
                () -> assertEquals(existingTorneo.getModalidad(), res.getModalidad()),
                () -> assertEquals(existingTorneo.getCategoria(), res.getCategoria()),
                () -> assertEquals(existingTorneo.getSuperficie(), res.getSuperficie()),
                () -> assertEquals(existingTorneo.getEntradas(), res.getEntradas()),
                () -> assertEquals(existingTorneo.getPremio(), res.getPremio()),
                () -> assertEquals(existingTorneo.getFechaInicio(), res.getFechaInicio()),
                () -> assertEquals(existingTorneo.getFechaFinalizacion(), res.getFechaFinalizacion()),
                () -> assertEquals(existingTorneo.getTenistas(), res.getTenistas())
        );
    }
}