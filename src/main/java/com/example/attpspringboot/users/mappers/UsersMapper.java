package com.example.attpspringboot.users.mappers;

import com.example.attpspringboot.users.dtos.UserCreateDto;
import com.example.attpspringboot.users.dtos.UserResponse;
import com.example.attpspringboot.users.dtos.UserUpdateDto;
import com.example.attpspringboot.users.models.User;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public User toEntity(UserCreateDto dto){
        User user = User.builder()
                .nombre(dto.getNombre())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rols(dto.getRols())
                .build();
        return user;
    }

    public User toEntity(UserUpdateDto dto, User existingUser){
        if (existingUser != null){
            existingUser.setNombre(dto.getNombre() != null ? dto.getNombre() : existingUser.getNombre());
            existingUser.setUsername(dto.getUsername() != null ? dto.getUsername() : existingUser.getUsername());
            existingUser.setEmail(dto.getEmail() != null ? dto.getEmail() : existingUser.getEmail());
            existingUser.setPassword(dto.getPassword() != null ? dto.getPassword() : existingUser.getPassword());
            existingUser.setRols(dto.getRols() != null ? dto.getRols() : existingUser.getRols());
            return existingUser;
        }else {
            User user = User.builder()
                    .nombre(dto.getNombre())
                    .username(dto.getUsername())
                    .email(dto.getEmail())
                    .password(dto.getPassword())
                    .rols(dto.getRols())
                    .build();
            return user;
        }
    }

    public UserResponse toDto(User user){
        UserResponse dto = UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .username(user.getUsername())
                .email(user.getEmail())
                .rols(user.getRols())
                .build();
        return dto;
    }
}
