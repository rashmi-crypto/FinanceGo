package com.rashmi.FinanceGo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtServiceTest {
    private final JwtService jwtService =
            new JwtService("0123456789abcdef0123456789abcdef", 3600000);

    @Test
    void generateToken_returnsThreePartToken() {
        String token = jwtService.generateToken("a@example.com");
        String[] tk=token.split("\\.");
        assertEquals(3,tk.length);
    }
    @Test
    void generateToken_hasCorrectSubject() {
        String secret = "0123456789abcdef0123456789abcdef";
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = jwtService.generateToken("a@example.com");

        // Act
        String subject = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        assertEquals("a@example.com",subject);
    }
}