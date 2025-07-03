package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<Users> testUsers;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUsers = Arrays.asList(
            new Users(1L, "John Doe", "john@example.com"),
            new Users(2L, "Jane Smith", "jane@test.com")
        );
    }

    @Test
    public void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(testUsers);

        List<Users> result = userController.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(userService).getAllUsers();
    }

    @Test
    public void testAddUser() {
        Users newUser = new Users(null, "Alice Brown", "alice@test.com");
        
        String result = userController.addUser(newUser);
        
        assertEquals("Users added", result);
        verify(userService).addUser(newUser);
    }

    @Test
    public void testGetUserById() {
        Users user = testUsers.get(0);
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Users> result = userController.getUserById(1L);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(user, result.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userService.findUserById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Users> result = userController.getUserById(999L);

        assertEquals(404, result.getStatusCodeValue());
        assertNull(result.getBody());
    }

    @Test
    public void testGetUsersByDomain() {
        List<Users> domainUsers = Arrays.asList(testUsers.get(0));
        when(userService.getUsersByDomain("example.com")).thenReturn(domainUsers);

        List<Users> result = userController.getUsersByDomain("example.com");

        assertEquals(1, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
        verify(userService).getUsersByDomain("example.com");
    }

    @Test
    public void testGetSortedUsers() {
        when(userService.sortUsersBy("name")).thenReturn(testUsers);

        List<Users> result = userController.getSortedUsers("name");

        assertEquals(2, result.size());
        verify(userService).sortUsersBy("name");
    }

    @Test
    public void testGetGroupedByDomain() {
        Map<String, List<Users>> groupedUsers = new HashMap<>();
        groupedUsers.put("example.com", Arrays.asList(testUsers.get(0)));
        groupedUsers.put("test.com", Arrays.asList(testUsers.get(1)));
        
        when(userService.groupByEmailDomain()).thenReturn(groupedUsers);

        Map<String, List<Users>> result = userController.getGroupedByDomain();

        assertEquals(2, result.size());
        assertTrue(result.containsKey("example.com"));
        verify(userService).groupByEmailDomain();
    }

    @Test
    public void testGetEmailDomainCounts() {
        Map<String, Long> domainCounts = new HashMap<>();
        domainCounts.put("example.com", 1L);
        domainCounts.put("test.com", 1L);
        
        when(userService.getEmailDomainCounts()).thenReturn(domainCounts);

        Map<String, Long> result = userController.getEmailDomainCounts();

        assertEquals(2, result.size());
        assertEquals(Long.valueOf(1), result.get("example.com"));
        verify(userService).getEmailDomainCounts();
    }

    @Test
    public void testSearchByName() {
        List<Users> searchResults = Arrays.asList(testUsers.get(0));
        when(userService.searchUsersByName("john")).thenReturn(searchResults);

        List<Users> result = userController.searchByName("john");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(userService).searchUsersByName("john");
    }

    @Test
    public void testGetUsersByEmailPattern() {
        List<Users> patternUsers = Arrays.asList(testUsers.get(0));
        when(userService.findUsersByEmailPattern(".*@example\\.com")).thenReturn(patternUsers);

        List<Users> result = userController.getUsersByEmailPattern(".*@example\\.com");

        assertEquals(1, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
        verify(userService).findUsersByEmailPattern(".*@example\\.com");
    }
}