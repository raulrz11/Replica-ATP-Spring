package com.example.attpspringboot.mappers;

import com.example.attpspringboot.dtos.tenistas.TenistaCreateDto;
import com.example.attpspringboot.dtos.tenistas.TenistaUpdateDto;
import com.example.attpspringboot.models.Tenista;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TenistasMapperTest {
    private final TenistasMapper mapper = new TenistasMapper();

    @Test
    void toEntity() {
        TenistaCreateDto dto = TenistaCreateDto.builder()
                .nombre("Roger Federer")
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .victorias(1242)
                .derrotas(274)
                .build();

        var res = mapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getNombre(), res.getNombre()),
                () -> assertEquals(dto.getPuntos(), res.getPuntos()),
                () -> assertEquals(dto.getPais(), res.getPais()),
                () -> assertEquals(dto.getFechaNacimiento(), res.getFechaNacimiento()),
                () -> assertEquals(dto.getAltura(), res.getAltura()),
                () -> assertEquals(dto.getPeso(), res.getPeso()),
                () -> assertEquals(dto.getInicioProfesional(), res.getInicioProfesional()),
                () -> assertEquals(dto.getManoBuena(), res.getManoBuena()),
                () -> assertEquals(dto.getReves(), res.getReves()),
                () -> assertEquals(dto.getEntrenador(), res.getEntrenador()),
                () -> assertEquals(dto.getPriceMoney(), res.getPriceMoney()),
                () -> assertEquals(dto.getVictorias(), res.getVictorias()),
                () -> assertEquals(dto.getDerrotas(), res.getDerrotas())
        );
    }

    @Test
    void testToEntity() {
        TenistaUpdateDto dto = TenistaUpdateDto.builder()
                .nombre("Roger Federer")
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .victorias(1242)
                .derrotas(274)
                .build();

        Tenista existingTenista = Tenista.builder()
                .nombre("Roger Federer")
                .ranking(5)
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .edad(42)
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .bestRanking(1)
                .victorias(1242)
                .derrotas(274)
                .WL("84.3%")
                .imagen("https://via.placeholder.com/150")
                .build();

        var res = mapper.toEntity(dto, existingTenista);

        assertAll(
                () -> assertEquals(dto.getNombre(), res.getNombre()),
                () -> assertEquals(dto.getPuntos(), res.getPuntos()),
                () -> assertEquals(dto.getPais(), res.getPais()),
                () -> assertEquals(dto.getFechaNacimiento(), res.getFechaNacimiento()),
                () -> assertEquals(dto.getAltura(), res.getAltura()),
                () -> assertEquals(dto.getPeso(), res.getPeso()),
                () -> assertEquals(dto.getInicioProfesional(), res.getInicioProfesional()),
                () -> assertEquals(dto.getManoBuena(), res.getManoBuena()),
                () -> assertEquals(dto.getReves(), res.getReves()),
                () -> assertEquals(dto.getEntrenador(), res.getEntrenador()),
                () -> assertEquals(dto.getPriceMoney(), res.getPriceMoney()),
                () -> assertEquals(dto.getVictorias(), res.getVictorias()),
                () -> assertEquals(dto.getDerrotas(), res.getDerrotas())
        );
    }

    @Test
    void toDto() {
        Tenista existingTenista = Tenista.builder()
                .nombre("Roger Federer")
                .ranking(5)
                .puntos(8760.5)
                .pais("Suiza")
                .fechaNacimiento(LocalDate.of(1981, 8, 8))
                .edad(42)
                .altura(1.85)
                .peso(85.5)
                .inicioProfesional(LocalDate.of(1998, 7, 29))
                .manoBuena(Tenista.Mano.DERECHA)
                .reves(Tenista.Reves.UNA_MANO)
                .entrenador("Ivan Ljubicic")
                .priceMoney(129946325.0)
                .bestRanking(1)
                .victorias(1242)
                .derrotas(274)
                .WL("84.3%")
                .imagen("https://via.placeholder.com/150")
                .build();

        var res = mapper.toDto(existingTenista);
        assertAll(
                () -> assertEquals(existingTenista.getNombre(), res.getNombre()),
                () -> assertEquals(existingTenista.getPuntos(), res.getPuntos()),
                () -> assertEquals(existingTenista.getPais(), res.getPais()),
                () -> assertEquals(existingTenista.getFechaNacimiento(), res.getFechaNacimiento()),
                () -> assertEquals(existingTenista.getAltura(), res.getAltura()),
                () -> assertEquals(existingTenista.getPeso(), res.getPeso()),
                () -> assertEquals(existingTenista.getInicioProfesional(), res.getInicioProfesional()),
                () -> assertEquals(existingTenista.getManoBuena(), res.getManoBuena()),
                () -> assertEquals(existingTenista.getReves(), res.getReves()),
                () -> assertEquals(existingTenista.getEntrenador(), res.getEntrenador()),
                () -> assertEquals(existingTenista.getPriceMoney(), res.getPriceMoney()),
                () -> assertEquals(existingTenista.getVictorias(), res.getVictorias()),
                () -> assertEquals(existingTenista.getDerrotas(), res.getDerrotas())
        );

    }
}