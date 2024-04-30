package com.example.attpspringboot.websockets;

import com.example.attpspringboot.models.Tenista;

import java.time.LocalDate;

public record TenistaNotification(
        Long id,
        String nombre,
        Integer ranking,
        Double puntos,
        String pais,
        String fechaNacimiento,
        Integer edad,
        Double altura,
        Double peso,
        String inicioProfesional,
        Tenista.Mano manoBuena,
        Tenista.Reves reves,
        String entrenador,
        String imagen,
        Double priceMoney,
        Integer bestRanking,
        String WL,
        String torneo
) {

}
