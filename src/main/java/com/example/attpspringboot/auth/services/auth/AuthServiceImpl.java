package com.example.attpspringboot.auth.services.auth;

import com.example.attpspringboot.auth.dtos.JwtAuthDto;
import com.example.attpspringboot.auth.dtos.SignIn;
import com.example.attpspringboot.auth.dtos.SignUp;
import com.example.attpspringboot.auth.exceptions.PassworNotMatchException;
import com.example.attpspringboot.auth.exceptions.UsernameAlreadyExistsException;
import com.example.attpspringboot.auth.repositories.AuthUsersRepository;
import com.example.attpspringboot.auth.services.jwt.JwtServiceImpl;
import com.example.attpspringboot.exceptions.TenistaBadRequestException;
import com.example.attpspringboot.exceptions.TorneoBadRequestException;
import com.example.attpspringboot.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthServiceImpl implements AuthService{
    private final AuthUsersRepository authUsersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(AuthUsersRepository authUsersRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.authUsersRepository = authUsersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthDto signUp(SignUp signUp) {
        if (!authUsersRepository.existsByUsername(signUp.getUsername())){
            if (signUp.getPassword().contentEquals(signUp.getPasswordComprobation())){
                User newUser = User.builder()
                        .nombre(signUp.getNombre())
                        .username(signUp.getUsername())
                        .email(signUp.getEmail())
                        .password(passwordEncoder.encode(signUp.getPassword()))
                        .rols(Stream.of(User.Rol.USER).collect(Collectors.toSet()))
                        .build();
                User savedUser = authUsersRepository.save(newUser);
                var jwt = jwtService.generateToken(savedUser);
                return JwtAuthDto.builder().token(jwt).build();
            }else {
                throw new PassworNotMatchException("Las contrasenas no coinciden");
            }
        }else {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya existe");
        }
    }

    @Override
    public JwtAuthDto signIn(SignIn signIn) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signIn.getUsername(), signIn.getPassword()));//se usa username porq es el q hemos especificado en el getUsername de Usuario
        User authUser = authUsersRepository.findByUsername(signIn.getUsername()).orElseThrow(() -> new TenistaBadRequestException("El usuario " + signIn.getUsername() + " no existe"));
        var jwt = jwtService.generateToken(authUser);
        return JwtAuthDto.builder().token(jwt).build();
    }
}
