package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.entity.User;
import com.rashmi.FinanceGo.exception.InvalidCredentialsException;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new InvalidCredentialsException());
        if( !passwordEncoder.matches(rawPassword , user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return jwtService.generateToken(user.getEmail());
    }
}
