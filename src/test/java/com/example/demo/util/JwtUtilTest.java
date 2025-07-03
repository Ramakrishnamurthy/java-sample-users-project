package com.example.demo.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

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
        
        Optional<String> extractedUsername = jwtUtil.extractUsername(token);
        
        assertTrue(extractedUsername.isPresent());
        assertEquals(TEST_USERNAME, extractedUsername.get());
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
        
        Optional<String> username1 = jwtUtil.extractUsername(token1);
        Optional<String> username2 = jwtUtil.extractUsername(token2);
        
        assertTrue(username1.isPresent());
        assertTrue(username2.isPresent());
        assertEquals("user1", username1.get());
        assertEquals("user2", username2.get());
    }

    @Test
    public void testInvalidToken() {
        String invalidToken = "invalid.token.here";
        
        Optional<String> extractedUsername = jwtUtil.extractUsername(invalidToken);
        
        assertFalse(extractedUsername.isPresent());
    }
}