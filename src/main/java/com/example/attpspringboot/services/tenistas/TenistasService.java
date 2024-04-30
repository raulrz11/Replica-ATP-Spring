package com.example.attpspringboot.services.tenistas;

import com.example.attpspringboot.dtos.tenistas.TenistaCreateDto;
import com.example.attpspringboot.dtos.tenistas.TenistaResponse;
import com.example.attpspringboot.dtos.tenistas.TenistaUpdateDto;
import com.example.attpspringboot.models.Tenista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

public interface TenistasService {
    Page<TenistaResponse> findAll(Optional<String> nombre, Optional<Integer> ranking,
                          Optional<Double> puntos, Optional<String> pais,
                          Optional<Date> fechaNacimiento, Optional<Integer> edad,
                          Optional<Double> altura, Optional<Double> peso,
                          Optional<Date> inicioProfesional, Optional<String> entrenador,
                          Optional<Tenista.Mano> manoBuena, Optional<Tenista.Reves> reves,
                          Optional<Double> priceMoney, Optional<Integer> bestRanking,
                          Optional<String> WL, Optional<Integer> victorias,
                          Optional<Integer> derrotas, Pageable pageable);

    TenistaResponse findById(Long id);

    TenistaResponse save(TenistaCreateDto dto);

    TenistaResponse update(TenistaUpdateDto dto, Long id);

    TenistaResponse updateImage(Long id, MultipartFile image, Boolean withUrl);

    void deleteById(Long id);

}
