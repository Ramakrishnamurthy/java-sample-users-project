package com.example.demo.service;

import com.example.demo.model.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceSimpleTest {

    @Test
    @DisplayName("Filter users by email domain using streams")
    void testGetUsersByDomainLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        String domain = "example.com";
        List<Users> result = users.stream()
            .filter(u -> u.getEmail() != null && u.getEmail().endsWith("@" + domain))
            .toList();
        assertEquals(2, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("bob@example.com", result.get(1).getEmail());
    }

    @Test
    @DisplayName("Sort users by name using lambda")
    void testSortUsersByNameLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Alice Smith", "alice@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        users.sort(Comparator.comparing(Users::getName));
        
        assertEquals("Alice Smith", users.get(0).getName());
        assertEquals("Bob Johnson", users.get(1).getName());
        assertEquals("John Doe", users.get(2).getName());
    }

    @Test
    @DisplayName("Group users by email domain using streams")
    void testGroupByEmailDomainLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        Map<String, List<Users>> map = users.stream()
            .filter(u -> u.getEmail() != null)
            .collect(java.util.stream.Collectors.groupingBy(u -> u.getEmail().split("@")[1]));
        assertEquals(2, map.size());
        assertTrue(map.containsKey("example.com"));
        assertTrue(map.containsKey("test.com"));
        assertEquals(2, map.get("example.com").size());
        assertEquals(1, map.get("test.com").size());
    }

    @Test
    @DisplayName("Search users by name using streams")
    void testSearchUsersByNameLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        String keyword = "john";
        List<Users> result = users.stream()
            .filter(u -> u.getName() != null && u.getName().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains("john"));
        assertTrue(result.get(1).getName().toLowerCase().contains("john"));
    }
}