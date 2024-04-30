package com.example.attpspringboot.auth.repositories;

import com.example.attpspringboot.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String nombre);
    boolean existsByUsername(String username);
}