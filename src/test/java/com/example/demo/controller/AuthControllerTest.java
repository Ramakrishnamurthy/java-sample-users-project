package com.example.demo.controller;

import com.example.demo.util.JwtUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Test
    void testAuthControllerExists() {
        AuthController controller = new AuthController();
        assertNotNull(controller);
    }

    @Test
    void testAuthenticateWithNullDependencies() {
        AuthController controller = new AuthController();
        assertThrows(NullPointerException.class, () -> controller.authenticate("user", "password"));
    }
}