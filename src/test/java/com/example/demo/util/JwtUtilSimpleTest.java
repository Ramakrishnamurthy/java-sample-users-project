package com.example.demo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilSimpleTest {

    @Test
    @DisplayName("JwtUtil creation is not null")
    void testJwtUtilCreation() {
        JwtUtil jwtUtil = new JwtUtil();
        assertNotNull(jwtUtil);
    }

    @Test
    @DisplayName("Generate token is not null and not empty")
    void testGenerateTokenNotNull() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("testuser");
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Extract username from token")
    void testExtractUsernameFromToken() {
        JwtUtil jwtUtil = new JwtUtil();
        String username = "testuser";
        String token = jwtUtil.generateToken(username);
        
        String extractedUsername = jwtUtil.extractUsername(token);
        
        assertEquals(username, extractedUsername);
    }

    @Test
    @DisplayName("Token validation returns true for valid token")
    void testTokenValidation() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("testuser");
        
        boolean isValid = jwtUtil.isTokenValid(token);
        
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Different users generate different tokens")
    void testDifferentUsersGenerateDifferentTokens() {
        JwtUtil jwtUtil = new JwtUtil();
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        
        assertNotEquals(token1, token2);
    }
}