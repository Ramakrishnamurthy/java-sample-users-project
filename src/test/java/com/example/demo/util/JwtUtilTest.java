package com.example.demo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    @DisplayName("Generate token is not null and not empty")
    void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Extract username from token")
    void testExtractUsername() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        
        String extractedUsername = jwtUtil.extractUsername(token);
        
        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    @DisplayName("Token validation returns true for valid token")
    void testIsTokenValid() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        
        boolean isValid = jwtUtil.isTokenValid(token);
        
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Different users generate different tokens and correct usernames")
    void testGenerateTokenForDifferentUsers() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        
        assertNotEquals(token1, token2);
        
        assertEquals("user1", jwtUtil.extractUsername(token1));
        assertEquals("user2", jwtUtil.extractUsername(token2));
    }

    @Test
    @DisplayName("Extracting username from invalid token throws exception")
    void testInvalidToken() {
        String invalidToken = "invalid.token.here";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
    }
}