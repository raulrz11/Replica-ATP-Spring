package com.example.attpspringboot.users.services;

import com.example.attpspringboot.exceptions.TenistaNotFoundException;
import com.example.attpspringboot.users.dtos.UserCreateDto;
import com.example.attpspringboot.users.dtos.UserResponse;
import com.example.attpspringboot.users.dtos.UserUpdateDto;
import com.example.attpspringboot.users.mappers.UsersMapper;
import com.example.attpspringboot.users.models.User;
import com.example.attpspringboot.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService{
    private final UsersRepository usersRepository;
    private  final UsersMapper usersMapper;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public Page<UserResponse> findAll(Optional<String> nombre, Optional<String> username, Optional<String> email, Pageable pageable) {
        Specification<User> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<User> specUsername = (root, query, criteriaBuilder) ->
                username.map(u -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + u.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<User> specEmail = (root, query, criteriaBuilder) ->
                email.map(e -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + e.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> criterios = Specification.where(specNombre).and(specUsername).and(specEmail);
        Page<UserResponse> lista = usersRepository.findAll(criterios, pageable).map(usersMapper::toDto);
        return lista;
    }

    @Override
    public UserResponse findById(Long id) {
        User existingUser = usersRepository.findById(id).orElse(null);
        if (existingUser != null){
            return usersMapper.toDto(existingUser);
        }else {
            throw new TenistaNotFoundException("El usuario con id " + id + " no existe");
        }
    }

    @Override
    public UserResponse ceate(UserCreateDto dto) {
        User userMapped = usersMapper.toEntity(dto);
        User newUser = usersRepository.save(userMapped);
        return usersMapper.toDto(newUser);
    }

    @Override
    public UserResponse update(UserUpdateDto dto, Long id) {
        User existingUser = usersRepository.findById(id).orElse(null);
        if (existingUser != null){
            User userMapped = usersMapper.toEntity(dto, existingUser);
            User updateUser = usersRepository.save(userMapped);
            return usersMapper.toDto(updateUser);
        }else {
            throw new TenistaNotFoundException("El usuario con id " + id + " no existe");
        }
    }

    @Override
    public void deleteById(Long id) {
        User existingUser = usersRepository.findById(id).orElse(null);
        if (existingUser != null){
            usersRepository.deleteById(id);
        }else {
            throw new TenistaNotFoundException("El usuario con id " + id + " no existe");
        }
    }
}
