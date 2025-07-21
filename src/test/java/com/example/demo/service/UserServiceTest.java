package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private List<Users> testUsers;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService();
        // Use reflection to set the mock repository
        try {
            java.lang.reflect.Field field = UserService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            field.set(userService, userRepository);
        } catch (Exception e) {
            // Handle reflection exception
        }
        
        testUsers = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com"),
            new Users(3L, "Bob Johnson", "bob@example.com")
        );
    }

    @Test
    @DisplayName("Get all users returns correct list")
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getAllUsers();
        
        assertEquals(3, result.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Add user calls repository save")
    void testAddUser() {
        Users newUser = new Users(4L, "Alice Brown", "alice@test.com");
        
        userService.addUser(newUser);
        
        verify(userRepository).save(newUser);
    }

    @Test
    @DisplayName("Get users by domain returns correct filtered list")
    void testGetUsersByDomain() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getUsersByDomain("example.com");
        
        assertEquals(2, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("bob@example.com", result.get(1).getEmail());
    }

    @Test
    @DisplayName("Get users by domain returns empty list for no match")
    void testGetUsersByDomainNoMatch() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getUsersByDomain("nonexistent.com");
        
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Sort users by name returns sorted list")
    void testSortUsersByName() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.sortUsersBy("name");
        
        assertEquals("Bob Johnson", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        assertEquals("John Doe", result.get(2).getName());
    }

    @Test
    @DisplayName("Sort users by email returns sorted list")
    void testSortUsersByEmail() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.sortUsersBy("email");
        
        assertEquals("bob@example.com", result.get(0).getEmail());
        assertEquals("jane@test.com", result.get(1).getEmail());
        assertEquals("john@example.com", result.get(2).getEmail());
    }

    @Test
    @DisplayName("Group users by email domain returns correct map")
    void testGroupByEmailDomain() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        Map<String, List<Users>> result = userService.groupByEmailDomain();
        
        assertEquals(2, result.size());
        assertTrue(result.containsKey("example.com"));
        assertTrue(result.containsKey("test.com"));
        assertEquals(2, result.get("example.com").size());
        assertEquals(1, result.get("test.com").size());
    }

    @Test
    @DisplayName("Search users by name returns correct filtered list")
    void testSearchUsersByName() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.searchUsersByName("john");
        
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains("john"));
        assertTrue(result.get(1).getName().toLowerCase().contains("john"));
    }

    @Test
    @DisplayName("Search users by name returns empty list for no match")
    void testSearchUsersByNameNoMatch() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.searchUsersByName("xyz");
        
        assertEquals(0, result.size());
    }
}