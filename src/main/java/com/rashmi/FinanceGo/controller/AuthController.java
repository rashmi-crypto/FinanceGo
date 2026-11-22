package com.rashmi.FinanceGo.controller;

import com.rashmi.FinanceGo.dto.LoginRequest;
import com.rashmi.FinanceGo.dto.LoginResponse;
import com.rashmi.FinanceGo.dto.RegisterRequest;
import com.rashmi.FinanceGo.dto.UserResponse;
import com.rashmi.FinanceGo.entity.User;
import com.rashmi.FinanceGo.service.AuthService;
import com.rashmi.FinanceGo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
    @RequestMapping("/api/auth")
    public class AuthController {

        private final UserService userService;
        private final AuthService authService;

        public AuthController(UserService userService, AuthService authService) {

            this.userService = userService;
            this.authService=authService;
        }

        @PostMapping("/register")
        public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
            User user= userService.register(request.email(),request.password(),request.fullName());
            UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getCreatedAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
            String token= authService.login(request.email(),request.password());
            LoginResponse response = new LoginResponse(token);
                    return ResponseEntity.ok(response);
        }
        @GetMapping("/me")
        public Map<String, String> me(Authentication authentication) {
            return Map.of("email", authentication.getName());
        }
    }
