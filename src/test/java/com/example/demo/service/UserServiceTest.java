package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private List<Users> testUsers;

    @Before
    public void setUp() {
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
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getAllUsers();
        
        assertEquals(3, result.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testAddUser() {
        Users newUser = new Users(4L, "Alice Brown", "alice@test.com");
        
        userService.addUser(newUser);
        
        verify(userRepository).save(newUser);
    }

    @Test
    public void testGetUsersByDomain() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getUsersByDomain("example.com");
        
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Users::getEmail)
                .allMatch(email -> email.endsWith("@example.com")));
    }

    @Test
    public void testGetUsersByDomainNoMatch() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.getUsersByDomain("nonexistent.com");
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortUsersByName() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.sortUsersBy("name");
        
        List<String> expectedOrder = Arrays.asList("Bob Johnson", "Jane Smith", "John Doe");
        List<String> actualOrder = result.stream()
                .map(Users::getName)
                .collect(Collectors.toList());
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testSortUsersByEmail() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.sortUsersBy("email");
        
        List<String> expectedOrder = Arrays.asList("bob@example.com", "jane@test.com", "john@example.com");
        List<String> actualOrder = result.stream()
                .map(Users::getEmail)
                .collect(Collectors.toList());
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testGroupByEmailDomain() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        Map<String, List<Users>> result = userService.groupByEmailDomain();
        
        assertEquals(2, result.size());
        assertTrue(result.containsKey("example.com"));
        assertTrue(result.containsKey("test.com"));
        assertEquals(2, result.get("example.com").size());
        assertEquals(1, result.get("test.com").size());
    }

    @Test
    public void testSearchUsersByName() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.searchUsersByName("john");
        
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Users::getName)
                .allMatch(name -> name.toLowerCase().contains("john")));
    }

    @Test
    public void testSearchUsersByNameNoMatch() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.searchUsersByName("xyz");
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindUserById() {
        Users user = testUsers.get(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        Optional<Users> result = userService.findUserById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<Users> result = userService.findUserById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindUsersByEmailPattern() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        List<Users> result = userService.findUsersByEmailPattern(".*@example\\.com");
        
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .map(Users::getEmail)
                .allMatch(email -> email.endsWith("@example.com")));
    }

    @Test
    public void testGetEmailDomainCounts() {
        when(userRepository.findAll()).thenReturn(testUsers);
        
        Map<String, Long> result = userService.getEmailDomainCounts();
        
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(2), result.get("example.com"));
        assertEquals(Long.valueOf(1), result.get("test.com"));
    }
}