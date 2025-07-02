package com.example.demo.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_USERNAME = "testuser";

    @Before
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
        
        String extractedUsername = jwtUtil.extractUsername(token);
        
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
        
        assertEquals("user1", jwtUtil.extractUsername(token1));
        assertEquals("user2", jwtUtil.extractUsername(token2));
    }

    @Test(expected = Exception.class)
    public void testInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        jwtUtil.extractUsername(invalidToken);
    }
}