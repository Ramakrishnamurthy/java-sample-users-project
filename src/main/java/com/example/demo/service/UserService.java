
package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }


    public void addUser(Users users) {
        userRepository.save(users);
    }


    public List<Users> getUsersByDomain(String domain) {
        return userRepository.findAll().stream()
                .filter(u -> Optional.ofNullable(u.getEmail())
                        .map(email -> email.endsWith("@" + domain)).orElse(false))
                .toList();
    }

    public List<Users> sortUsersBy(String field) {
        var users = new ArrayList<>(userRepository.findAll());
        users.sort((u1, u2) -> switch (field) {
            case "name" -> u1.getName().compareTo(u2.getName());
            case "email" -> u1.getEmail().compareTo(u2.getEmail());
            default -> 0;
        });
        return users;
    }

    public Map<String, List<Users>> groupByEmailDomain() {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().contains("@"))
                .collect(java.util.stream.Collectors.groupingBy(u -> u.getEmail().split("@", 2)[1]));
    }

    public List<Users> searchUsersByName(String keyword) {
        return userRepository.findAll().stream()
                .filter(u -> Optional.ofNullable(u.getName())
                        .map(name -> name.toLowerCase().contains(keyword.toLowerCase())).orElse(false))
                .toList();
    }
}
