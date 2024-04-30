package com.example.attpspringboot.users.repositories;

import com.example.attpspringboot.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
