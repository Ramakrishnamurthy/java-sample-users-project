package com.example.demo.service;

import com.example.demo.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceSimpleTest {

    private List<Users> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Alice Smith", "alice@test.com")
        );
    }

    @Test
    void testFindUserByEmail() {
        Optional<Users> user = users.stream().filter(u -> "jane@test.com".equals(u.getEmail())).findFirst();
        assertTrue(user.isPresent());
        assertEquals("Jane Smith", user.get().getName());
    }

    @Test
    void testNoUserFound() {
        Optional<Users> user = users.stream().filter(u -> "notfound@test.com".equals(u.getEmail())).findFirst();
        assertFalse(user.isPresent());
    }
}