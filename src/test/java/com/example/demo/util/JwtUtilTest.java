package com.example.demo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateTokenNotNull() {
        String token = jwtUtil.generateToken("testuser");
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void testExtractUsernameFromToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testTokenValidation() {
        String token = jwtUtil.generateToken("testuser");
        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void testDifferentUsersGenerateDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        assertNotEquals(token1, token2);
    }
}