package com.example.attpspringboot.mappers;

import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.websockets.TenistaNotification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public TenistaNotification toNotification(Tenista tenista){
        return new TenistaNotification(
                tenista.getId(), tenista.getNombre(),tenista.getRanking(), tenista.getPuntos(),
                tenista.getPais(), tenista.getFechaNacimiento().toString(),tenista.getEdad(), tenista.getAltura(),
                tenista.getPeso(), tenista.getInicioProfesional().toString(),tenista.getManoBuena(),
                tenista.getReves(),tenista.getEntrenador(), tenista.getImagen(),
                tenista.getPriceMoney(), tenista.getBestRanking(),tenista.getWL(), tenista.getTorneo().getNombre()
        );
    }
}
