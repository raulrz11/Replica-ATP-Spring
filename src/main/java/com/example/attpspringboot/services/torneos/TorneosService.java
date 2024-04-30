package com.example.attpspringboot.services.torneos;

import com.example.attpspringboot.dtos.torneos.TorneoCreateDto;
import com.example.attpspringboot.dtos.torneos.TorneoRespose;
import com.example.attpspringboot.dtos.torneos.TorneoUpdateDto;
import com.example.attpspringboot.models.Torneo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

public interface TorneosService {
    Page<TorneoRespose> findAll(Optional<String> nombre, Optional<String> ubicacion,
                                Optional<Torneo.Modalidad> modalidad, Optional<Torneo.Categoria> categoria,
                                Optional<Torneo.Superficie> superficie, Optional<Integer> entradas,
                                Optional<Double> premio, Optional<LocalDate> fechaInicio,
                                Optional<LocalDate> fechaFinalizacion, Pageable pageable);

    TorneoRespose findById(Long id);

    TorneoRespose create(TorneoCreateDto dto);

    TorneoRespose update(TorneoUpdateDto dto, Long id);

    TorneoRespose updateImage(Long id, MultipartFile image, Boolean withUrl);

    void deleteById(Long id);

}
