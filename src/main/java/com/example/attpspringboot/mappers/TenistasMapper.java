package com.example.attpspringboot.mappers;

import com.example.attpspringboot.dtos.tenistas.TenistaCreateDto;
import com.example.attpspringboot.dtos.tenistas.TenistaResponse;
import com.example.attpspringboot.dtos.tenistas.TenistaUpdateDto;
import com.example.attpspringboot.models.Tenista;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Component
public class TenistasMapper {
    public Tenista toEntity(TenistaCreateDto dto){
        Tenista tenista = Tenista.builder()
                .nombre(dto.getNombre())
                .puntos(dto.getPuntos())
                .pais(dto.getPais())
                .fechaNacimiento(dto.getFechaNacimiento())
                .edad(Period.between(dto.getFechaNacimiento(), LocalDate.now()).getYears())
                .altura(dto.getAltura())
                .peso(dto.getPeso())
                .inicioProfesional(dto.getInicioProfesional())
                .manoBuena(dto.getManoBuena())
                .reves(dto.getReves())
                .entrenador(dto.getEntrenador())
                .priceMoney(dto.getPriceMoney())
                .victorias(dto.getVictorias())
                .derrotas(dto.getDerrotas())
                .build();
        return tenista;
    }

    public Tenista toEntity(TenistaUpdateDto dto, Tenista existingTenista){
        if(existingTenista != null) {
            existingTenista.setNombre(dto.getNombre() != null ? dto.getNombre() : existingTenista.getNombre());
            existingTenista.setRanking(dto.getRanking() != null ? dto.getRanking() : existingTenista.getRanking());
            existingTenista.setPuntos(dto.getPuntos() != null ? dto.getPuntos() : existingTenista.getPuntos());
            existingTenista.setPais(dto.getPais() != null ? dto.getPais() : existingTenista.getPais());
            existingTenista.setFechaNacimiento(dto.getFechaNacimiento() != null ? dto.getFechaNacimiento() : existingTenista.getFechaNacimiento());
            existingTenista.setEdad(dto.getEdad() != null ? dto.getEdad() : existingTenista.getEdad());
            existingTenista.setAltura(dto.getAltura() != null ? dto.getAltura() : existingTenista.getAltura());
            existingTenista.setPeso(dto.getPeso() != null ? dto.getPeso() : existingTenista.getPeso());
            existingTenista.setInicioProfesional(dto.getInicioProfesional() != null ? dto.getInicioProfesional() : existingTenista.getInicioProfesional());
            existingTenista.setManoBuena(dto.getManoBuena() != null ? dto.getManoBuena() : existingTenista.getManoBuena());
            existingTenista.setReves(dto.getReves() != null ? dto.getReves() : existingTenista.getReves());
            existingTenista.setEntrenador(dto.getEntrenador() != null ? dto.getEntrenador() : existingTenista.getEntrenador());
            existingTenista.setImagen(dto.getImagen() != null ? dto.getImagen() : existingTenista.getImagen());
            existingTenista.setPriceMoney(dto.getPriceMoney() != null ? dto.getPriceMoney() : existingTenista.getPriceMoney());
            existingTenista.setBestRanking(dto.getBestRanking() != null ? dto.getBestRanking() : existingTenista.getBestRanking());
            existingTenista.setVictorias(dto.getVictorias() != null ? dto.getVictorias() : existingTenista.getVictorias());
            existingTenista.setDerrotas(dto.getDerrotas() != null ? dto.getDerrotas() : existingTenista.getDerrotas());
            existingTenista.setWL(dto.getWL() != null ? dto.getWL() : existingTenista.getWL());
            return existingTenista;
        } else {
            return Tenista.builder()
                    .nombre(dto.getNombre())
                    .ranking(dto.getRanking())
                    .puntos(dto.getPuntos())
                    .pais(dto.getPais())
                    .fechaNacimiento(dto.getFechaNacimiento())
                    .edad(dto.getEdad())
                    .altura(dto.getAltura())
                    .peso(dto.getPeso())
                    .inicioProfesional(dto.getInicioProfesional())
                    .manoBuena(dto.getManoBuena())
                    .reves(dto.getReves())
                    .entrenador(dto.getEntrenador())
                    .imagen(dto.getImagen())
                    .priceMoney(dto.getPriceMoney())
                    .bestRanking(dto.getBestRanking())
                    .victorias(dto.getVictorias())
                    .derrotas(dto.getDerrotas())
                    .WL(dto.getWL())
                    .build();
        }
    }

    public TenistaResponse toDto(Tenista tenista){
        TenistaResponse dto = TenistaResponse.builder()
                .nombre(tenista.getNombre())
                .ranking(tenista.getRanking())
                .puntos(tenista.getPuntos())
                .pais(tenista.getPais())
                .fechaNacimiento(tenista.getFechaNacimiento())
                .edad(tenista.getEdad())
                .altura(tenista.getAltura())
                .peso(tenista.getPeso())
                .inicioProfesional(tenista.getInicioProfesional())
                .manoBuena(tenista.getManoBuena())
                .reves(tenista.getReves())
                .entrenador(tenista.getEntrenador())
                .imagen(tenista.getImagen())
                .priceMoney(tenista.getPriceMoney())
                .bestRanking(tenista.getBestRanking())
                .victorias(tenista.getVictorias())
                .derrotas(tenista.getDerrotas())
                .WL(tenista.getWL())
                .torneo(tenista.getTorneo() != null ? tenista.getTorneo().getNombre() : "SIN TORNEO")
                .build();
        return dto;
    }
}
