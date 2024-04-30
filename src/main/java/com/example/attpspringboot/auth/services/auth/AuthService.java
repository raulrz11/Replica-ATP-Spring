package com.example.attpspringboot.auth.services.auth;

import com.example.attpspringboot.auth.dtos.JwtAuthDto;
import com.example.attpspringboot.auth.dtos.SignIn;
import com.example.attpspringboot.auth.dtos.SignUp;

public interface AuthService {
    JwtAuthDto signIn(SignIn signIn);

    JwtAuthDto signUp(SignUp signUp);
}
