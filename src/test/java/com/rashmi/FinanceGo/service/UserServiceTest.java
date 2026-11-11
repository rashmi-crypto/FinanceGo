package com.rashmi.FinanceGo.service;

import com.rashmi.FinanceGo.entity.User;
import com.rashmi.FinanceGo.exception.EmailAlreadyExistsException;
import com.rashmi.FinanceGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    @Test
    void register_hashesPasswordAndSavesUser() {
        when(userRepository.existsByEmail("a@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        User result = userService.register("a@example.com", "secret123", "Alice");

        assertEquals("a@example.com",result.getEmail());
        assertEquals("hashed-secret",result.getPasswordHash());
        assertNotEquals("secret123",result.getPasswordHash());
    }
    @Test
    void register_throws_whenEmailExists()
    {
        when(userRepository.existsByEmail("a@example.com")).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.register("a@example.com", "secret123", "Alice"));
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder);
    }
}
