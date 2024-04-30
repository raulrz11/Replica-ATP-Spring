package com.example.attpspringboot.auth.services.auth;

import com.example.attpspringboot.auth.dtos.JwtAuthDto;
import com.example.attpspringboot.auth.dtos.SignIn;
import com.example.attpspringboot.auth.dtos.SignUp;
import com.example.attpspringboot.auth.exceptions.PassworNotMatchException;
import com.example.attpspringboot.auth.exceptions.UsernameAlreadyExistsException;
import com.example.attpspringboot.auth.repositories.AuthUsersRepository;
import com.example.attpspringboot.auth.services.jwt.JwtServiceImpl;
import com.example.attpspringboot.exceptions.TenistaBadRequestException;
import com.example.attpspringboot.users.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthUsersRepository authUsersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authenticationService;

    @Test
    public void signup_returnToken_whenPasswordsMatch() {
        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("raulito")
                .email("raul@gmail.com")
                .password("raul")
                .passwordComprobation("raul")
                .build();

        when(authUsersRepository.existsByUsername(request.getUsername())).thenReturn(false);

        User userStored = new User();
        when(authUsersRepository.save(any(User.class))).thenReturn(userStored);

        String token = "test_token";
        when(jwtService.generateToken(userStored)).thenReturn(token);

        JwtAuthDto response = authenticationService.signUp(request);

        assertAll("Sign Up",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authUsersRepository, times(1)).save(any(User.class)),
                () -> verify(jwtService, times(1)).generateToken(userStored)
        );
    }

    @Test
    public void signup_throwException_whenPasswordNotMatch() {
        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("realty")
                .email("raul@gmail.com")
                .password("raul")
                .passwordComprobation("raul1")
                .build();

        when(authUsersRepository.existsByUsername(request.getUsername())).thenReturn(false);

        assertNotEquals(request.getPassword(), request.getPasswordComprobation());
        assertThrows(PassworNotMatchException.class, () -> authenticationService.signUp(request));
    }

    @Test
    public void signup_throwException_whenUsernameAlreadyExists() {
        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("raul")
                .email("raul@gmail.com")
                .password("raul")
                .passwordComprobation("raul")
                .build();

        when(authUsersRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> authenticationService.signUp(request));
    }

    @Test
    public void  signIn_returnToken_whenInvalidParams() {
        SignIn request = SignIn.builder()
                .username("admin")
                .password("admin1")
                .build();

        User user = new User();
        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        String token = "test_token";
        when(jwtService.generateToken(user)).thenReturn(token);

        JwtAuthDto response = authenticationService.signIn(request);

        assertAll("Sign In",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class)),
                () -> verify(authUsersRepository, times(1)).findByUsername(request.getUsername()),
                () -> verify(jwtService, times(1)).generateToken(user)
        );
    }

    @Test
    public void signIn_throwException_whenInvalidParams() {
        SignIn request = SignIn.builder()
                .username("admin")
                .password("admin1")
                .build();

        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(TenistaBadRequestException.class, () -> authenticationService.signIn(request));
    }
}