package com.example.demo.controller;

import com.example.demo.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Test
    @DisplayName("AuthController can be instantiated")
    void testAuthControllerExists() {
        AuthController controller = new AuthController();
        assertNotNull(controller);
    }

    @Test
    @DisplayName("authenticate throws NullPointerException with null dependencies")
    void testAuthenticateWithNullDependencies() {
        AuthController controller = new AuthController();
        assertThrows(NullPointerException.class, () -> controller.authenticate("user", "password"));
    }
}