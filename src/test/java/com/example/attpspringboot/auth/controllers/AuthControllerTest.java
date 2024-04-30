package com.example.attpspringboot.auth.controllers;

import com.example.attpspringboot.auth.dtos.JwtAuthDto;
import com.example.attpspringboot.auth.dtos.SignIn;
import com.example.attpspringboot.auth.dtos.SignUp;
import com.example.attpspringboot.auth.exceptions.PassworNotMatchException;
import com.example.attpspringboot.auth.exceptions.UsernameAlreadyExistsException;
import com.example.attpspringboot.auth.services.auth.AuthServiceImpl;
import com.example.attpspringboot.exceptions.TenistaBadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private final String myEndpoint = "/auth";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private AuthServiceImpl authenticationService;


    @Autowired
    public AuthControllerTest(AuthServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    void signUp() throws Exception {
        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("raulito")
                .email("raul@gmail.com")
                .password("Spiderman1820")
                .passwordComprobation("Spiderman1820")
                .build();
        JwtAuthDto jwtAuthResponse = JwtAuthDto.builder().token("token").build();

        var myLocalEndpoint = myEndpoint + "/signup";

        when(authenticationService.signUp(any(SignUp.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse();

        JwtAuthDto res = mapper.readValue(response.getContentAsString(), JwtAuthDto.class);

        assertAll("signup",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );

        verify(authenticationService, times(1)).signUp(any(SignUp.class));
    }

    @Test
    void signUp_WhenPasswordsDoNotMatch_ShouldThrowException() {
        var myLocalEndpoint = myEndpoint + "/signup";

        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("raulito")
                .email("raul@gmail.com")
                .password("raul")
                .passwordComprobation("raul1")
                .build();

        when(authenticationService.signUp(any(SignUp.class))).thenThrow(new PassworNotMatchException("Las contrasenas no coinciden"));

        assertThrows(PassworNotMatchException.class, () -> authenticationService.signUp(request));

        verify(authenticationService, times(1)).signUp(any(SignUp.class));
    }

    @Test
    void signUp_WhenUsernameAlreadyExist_ShouldThrowException() {
        var myLocalEndpoint = myEndpoint + "/signup";

        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("raulito")
                .email("raul@gmail.com")
                .password("raul")
                .passwordComprobation("raul")
                .build();

        when(authenticationService.signUp(any(SignUp.class))).thenThrow(new UsernameAlreadyExistsException("El nombre de usuario ya existe"));

        assertThrows(UsernameAlreadyExistsException.class, () -> authenticationService.signUp(request));

        verify(authenticationService, times(1)).signUp(any(SignUp.class));
    }

    @Test
    void signIn() throws Exception {
        SignUp request = SignUp.builder()
                .nombre("Raul")
                .username("raulito")
                .email("raul@gmail.com")
                .password("raul")
                .passwordComprobation("raul")
                .build();
        JwtAuthDto jwtAuthResponse = JwtAuthDto.builder().token("token").build();

        var myLocalEndpoint = myEndpoint + "/signin";

        when(authenticationService.signIn(any(SignIn.class))).thenReturn(jwtAuthResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        post(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andReturn().getResponse();

        JwtAuthDto res = mapper.readValue(response.getContentAsString(), JwtAuthDto.class);

        assertAll("signin",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );

        verify(authenticationService, times(1)).signIn(any(SignIn.class));
    }

    @Test
    void signIn_Invalid() {
        var myLocalEndpoint = myEndpoint + "/signin";

        SignIn request = SignIn.builder()
                .username("admin")
                .password("<PASSWORD>")
                .build();

        when(authenticationService.signIn(any(SignIn.class))).thenThrow(new TenistaBadRequestException("El usuario " + request.getUsername() + " no existe"));

        assertThrows(TenistaBadRequestException.class, () -> authenticationService.signIn(request));

        verify(authenticationService, times(1)).signIn(any(SignIn.class));
    }

}