
package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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
                .filter(u -> Optional.ofNullable(u.getEmail()).map(email -> email.endsWith("@" + domain)).orElse(false))
                .toList();
    }

    public List<Users> sortUsersBy(final String field) {
        List<Users> users = userRepository.findAll();
        Comparator<Users> comparator = switch (field) {
            case "name" -> Comparator.comparing(Users::getName);
            case "email" -> Comparator.comparing(Users::getEmail);
            default -> (u1, u2) -> 0;
        };
        users.sort(comparator);
        return users;
    }

    public Map<String, List<Users>> groupByEmailDomain() {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail() != null)
                .collect(Collectors.groupingBy(u -> u.getEmail().split("@")[1]));
    }

    public List<Users> searchUsersByName(String keyword) {
        return userRepository.findAll().stream()
                .filter(u -> Optional.ofNullable(u.getName()).map(name -> name.toLowerCase().contains(keyword.toLowerCase())).orElse(false))
                .toList();
    }
}
