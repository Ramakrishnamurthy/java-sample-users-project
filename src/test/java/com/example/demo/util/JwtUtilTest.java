package com.example.demo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    public void testExtractUsername() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        String extractedUsername = jwtUtil.extractUsername(token).orElse(null);
        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    public void testIsTokenValid() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        
        boolean isValid = jwtUtil.isTokenValid(token);
        
        assertTrue(isValid);
    }

    @Test
    public void testGenerateTokenForDifferentUsers() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        
        assertNotEquals(token1, token2);
        
        assertEquals("user1", jwtUtil.extractUsername(token1).orElse(null));
        assertEquals("user2", jwtUtil.extractUsername(token2).orElse(null));
    }

    @Test
    public void testInvalidToken() {
        String invalidToken = "invalid.token.here";
        assertTrue(jwtUtil.extractUsername(invalidToken).isEmpty());
    }
}