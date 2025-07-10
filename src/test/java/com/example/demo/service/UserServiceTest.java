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
    @DisplayName("getAllUsers returns all users from repository")
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getAllUsers();
        
        assertEquals(3, result.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("addUser saves user to repository")
    void testAddUser() {
        Users newUser = new Users(4L, "Alice Brown", "alice@test.com");
        
        userService.addUser(newUser);
        
        verify(userRepository).save(newUser);
    }

    @Test
    @DisplayName("getUsersByDomain filters users by domain")
    void testGetUsersByDomain() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getUsersByDomain("example.com");
        
        assertEquals(2, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
        assertEquals("bob@example.com", result.get(1).getEmail());
    }

    @Test
    @DisplayName("getUsersByDomain returns empty list for no match")
    void testGetUsersByDomainNoMatch() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getUsersByDomain("nonexistent.com");
        
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("sortUsersBy sorts users by name")
    void testSortUsersByName() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.sortUsersBy("name");
        
        assertEquals("Bob Johnson", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        assertEquals("John Doe", result.get(2).getName());
    }

    @Test
    @DisplayName("sortUsersBy sorts users by email")
    void testSortUsersByEmail() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.sortUsersBy("email");
        
        assertEquals("bob@example.com", result.get(0).getEmail());
        assertEquals("jane@test.com", result.get(1).getEmail());
        assertEquals("john@example.com", result.get(2).getEmail());
    }

    @Test
    @DisplayName("groupByEmailDomain groups users by domain")
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
    @DisplayName("searchUsersByName finds users by name keyword")
    void testSearchUsersByName() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.searchUsersByName("john");
        
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains("john"));
        assertTrue(result.get(1).getName().toLowerCase().contains("john"));
    }

    @Test
    @DisplayName("searchUsersByName returns empty list for no match")
    void testSearchUsersByNameNoMatch() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.searchUsersByName("xyz");
        
        assertEquals(0, result.size());
    }
}