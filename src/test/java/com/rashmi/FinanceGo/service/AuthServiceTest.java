package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.entity.User;
import com.rashmi.FinanceGo.exception.InvalidCredentialsException;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;
    @Test
    void login_returnsToken_whenCredentialsValid() {
        // Arrange
        User user = new User();
        user.setEmail("a@example.com");
        user.setPasswordHash("stored-hash");

        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret1234", "stored-hash")).thenReturn(true);
        when(jwtService.generateToken("a@example.com")).thenReturn("fake-token");

        // Act
        String token = authService.login("a@example.com", "secret1234");
        assertEquals("fake-token",token);
    }
    @Test
    void login_throws_whenEmailNotFound()
    {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class,()->authService.login("notfound@example.com","secret1234"));
        verifyNoInteractions(passwordEncoder,jwtService);

    }
    @Test
    void login_throws_whenPasswordWrong()
    {
        User user=new User();
        user.setEmail("pwwrong@example.com");
        user.setPasswordHash("stored-hash");

        when(userRepository.findByEmail("pwwrong@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret1234", "stored-hash")).thenReturn(false);
        assertThrows(InvalidCredentialsException.class,()->authService.login("pwwrong@example.com","secret1234"));
        verifyNoInteractions(jwtService);
    }
}
