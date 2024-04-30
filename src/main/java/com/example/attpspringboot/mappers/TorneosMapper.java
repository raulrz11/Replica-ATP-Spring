package com.example.attpspringboot.mappers;

import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoRespose;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.models.Torneo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TorneosMapper {
    public Torneo toEntity(TorneoCreateDto dto){
        Torneo torneo = Torneo.builder()
                .nombre(dto.getNombre())
                .ubicacion(dto.getUbicacion())
                .modalidad(dto.getModalidad())
                .categoria(dto.getCategoria())
                .superficie(dto.getSuperficie())
                .entradas(dto.getEntradas())
                .premio(dto.getPremio())
                .fechaInicio(dto.getFechaInicio())
                .fechaFinalizacion(dto.getFechaFinalizacion())
                .tenistas(dto.getTenistas())
                .build();
        return torneo;
    }

    public Torneo toEntity(TorneoUpdateDto dto, Torneo existingTorneo){
        if (existingTorneo != null){
            existingTorneo.setNombre(dto.getNombre() != null ? dto.getNombre() : existingTorneo.getNombre());
            existingTorneo.setUbicacion(dto.getUbicacion() != null ? dto.getUbicacion() : existingTorneo.getUbicacion());
            existingTorneo.setModalidad(dto.getModalidad() != null ? dto.getModalidad() : existingTorneo.getModalidad());
            existingTorneo.setCategoria(dto.getCategoria() != null ? dto.getCategoria() : existingTorneo.getCategoria());
            existingTorneo.setSuperficie(dto.getSuperficie() != null ? dto.getSuperficie() : existingTorneo.getSuperficie());
            existingTorneo.setEntradas(dto.getEntradas() != null ? dto.getEntradas() : existingTorneo.getEntradas());
            existingTorneo.setPremio(dto.getPremio() != null ? dto.getPremio() : existingTorneo.getPremio());
            existingTorneo.setFechaInicio(dto.getFechaInicio() != null ? dto.getFechaInicio() : existingTorneo.getFechaInicio());
            existingTorneo.setFechaFinalizacion(dto.getFechaFinalizacion() != null ? dto.getFechaFinalizacion() : existingTorneo.getFechaFinalizacion());
            existingTorneo.setImagen(dto.getImagen() != null ? dto.getImagen() : existingTorneo.getImagen());

        }
        return existingTorneo;
    }

    public TorneoRespose toDto(Torneo torneo){
        List<String> nombreTennistas = torneo.getTenistas().stream()
                .map(Tenista::getNombre)
                .collect(Collectors.toList());

        TorneoRespose dto = TorneoRespose.builder()
                .nombre(torneo.getNombre())
                .ubicacion(torneo.getUbicacion())
                .modalidad(torneo.getModalidad())
                .categoria(torneo.getCategoria())
                .superficie(torneo.getSuperficie())
                .entradas(torneo.getEntradas())
                .premio(torneo.getPremio())
                .fechaInicio(torneo.getFechaInicio())
                .fechaFinalizacion(torneo.getFechaFinalizacion())
                .imagen(torneo.getImagen())
                .tenistas(nombreTennistas)
                .build();
        return dto;
    }
}
