package com.example.demo.util;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class JwtUtilSimpleTest {

    @Test
    public void testJwtUtilCreation() {
        JwtUtil jwtUtil = new JwtUtil();
        assertNotNull(jwtUtil);
    }

    @Test
    public void testGenerateTokenNotNull() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("testuser");
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    public void testExtractUsernameFromToken() {
        JwtUtil jwtUtil = new JwtUtil();
        String username = "testuser";
        String token = jwtUtil.generateToken(username);
        
        Optional<String> extractedUsername = jwtUtil.extractUsername(token);
        
        assertTrue(extractedUsername.isPresent());
        assertEquals(username, extractedUsername.get());
    }

    @Test
    public void testTokenValidation() {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken("testuser");
        
        boolean isValid = jwtUtil.isTokenValid(token);
        
        assertTrue(isValid);
    }

    @Test
    public void testDifferentUsersGenerateDifferentTokens() {
        JwtUtil jwtUtil = new JwtUtil();
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        
        assertNotEquals(token1, token2);
    }
}