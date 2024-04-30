package com.example.attpspringboot.users.services;

import com.example.attpspringboot.users.dtos.UserCreateDto;
import com.example.attpspringboot.users.dtos.UserResponse;
import com.example.attpspringboot.users.dtos.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsersService {
    Page<UserResponse> findAll(Optional<String> nombre, Optional<String> username,
                               Optional<String> email, Pageable pageable);

    UserResponse findById(Long id);

    UserResponse ceate(UserCreateDto dto);

    UserResponse update(UserUpdateDto dto, Long id);

    void deleteById(Long id);
}
