package com.example.demo.service;

import com.example.demo.model.Users;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UserServiceSimpleTest {

    @Test
    public void testGetUsersByDomainLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        String domain = "example.com";
        
        List<Users> result = users.stream()
                .filter(user -> user.getEmailOptional()
                        .map(email -> email.endsWith("@" + domain))
                        .orElse(false))
                .collect(Collectors.toList());
        
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Users::getEmail)
                .allMatch(email -> email.endsWith("@example.com")));
    }

    @Test
    public void testSortUsersByNameLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Alice Smith", "alice@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        List<Users> sortedUsers = users.stream()
                .sorted((u1, u2) -> u1.getNameOptional()
                        .orElse("")
                        .compareTo(u2.getNameOptional().orElse("")))
                .collect(Collectors.toList());
        
        List<String> expectedOrder = Arrays.asList("Alice Smith", "Bob Johnson", "John Doe");
        List<String> actualOrder = sortedUsers.stream()
                .map(Users::getName)
                .collect(Collectors.toList());
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGroupByEmailDomainLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        Map<String, List<Users>> map = users.stream()
                .filter(user -> user.getEmailOptional().isPresent())
                .collect(Collectors.groupingBy(user -> {
                    String email = user.getEmailOptional().get();
                    String[] parts = email.split("@");
                    return parts.length == 2 ? parts[1] : "unknown";
                }));
        
        assertEquals(2, map.size());
        assertTrue(map.containsKey("example.com"));
        assertTrue(map.containsKey("test.com"));
        assertEquals(2, map.get("example.com").size());
        assertEquals(1, map.get("test.com").size());
    }

    @Test
    public void testSearchUsersByNameLogic() {
        List<Users> users = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
        
        String keyword = "john";
        
        List<Users> result = users.stream()
                .filter(user -> user.getNameOptional()
                        .map(name -> name.toLowerCase().contains(keyword.toLowerCase()))
                        .orElse(false))
                .collect(Collectors.toList());
        
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Users::getName)
                .allMatch(name -> name.toLowerCase().contains("john")));
    }
}