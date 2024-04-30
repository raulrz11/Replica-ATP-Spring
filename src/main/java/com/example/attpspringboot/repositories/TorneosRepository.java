package com.example.attpspringboot.repositories;

import com.example.attpspringboot.models.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TorneosRepository extends JpaRepository<Torneo, Long>, JpaSpecificationExecutor<Torneo> {
    Optional<Torneo> findByNombreEqualsIgnoreCase(String nombre);
}
