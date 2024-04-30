package com.example.attpspringboot.auth.services.users;

import com.example.attpspringboot.auth.repositories.AuthUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUsersServiceImpl implements AuthUsersService{
    private final AuthUsersRepository authUsersRepository;

    @Autowired
    public AuthUsersServiceImpl(AuthUsersRepository authUsersRepository) {
        this.authUsersRepository = authUsersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authUsersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));
    }
}
