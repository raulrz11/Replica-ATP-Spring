package com.example.attpspringboot;

import com.example.attpspringboot.models.Tenista;
import com.example.attpspringboot.repositories.TenistasRepository;
import com.example.attpspringboot.services.tenistas.TenistasServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class AttpSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AttpSpringBootApplication.class);
        ConfigurableApplicationContext context = app.run(args);

        /*TenistasServiceImpl tenistasService = context.getBean(TenistasServiceImpl.class);
        TenistasRepository tenistasRepository = context.getBean(TenistasRepository.class);
        List<Tenista> tenistas = tenistasRepository.findAll();
        tenistasService.actualizarRanking(tenistas);
        tenistasService.calcularPorcentajeVictoriasDerrotas();

        String password = "user1";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);*/
    }

}
