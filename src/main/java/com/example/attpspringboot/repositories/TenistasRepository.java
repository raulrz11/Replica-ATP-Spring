package com.example.attpspringboot.repositories;

import com.example.attpspringboot.models.Tenista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TenistasRepository extends JpaRepository<Tenista, Long>, JpaSpecificationExecutor<Tenista> {
}
