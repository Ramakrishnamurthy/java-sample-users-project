
package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public String addUser(@Valid @RequestBody Users users) {
        userService.addUser(users);
        return "Users added";
    }

    @GetMapping("/by-domain")
    public List<Users> getUsersByDomain(@RequestParam String domain) {
        return userService.getUsersByDomain(domain);
    }

    @GetMapping("/sorted")
    public List<Users> getSortedUsers(@RequestParam(defaultValue = "name") String field) {
        return userService.sortUsersBy(field);
    }

    @GetMapping("/grouped-by-domain")
    public Map<String, List<Users>> getGroupedByDomain() {
        return userService.groupByEmailDomain();
    }

    @GetMapping("/search")
    public List<Users> searchByName(@RequestParam String keyword) {
        return userService.searchUsersByName(keyword);
    }
}
