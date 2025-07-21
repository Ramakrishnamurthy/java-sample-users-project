
package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    public List<Users> sortUsersBy(final String field) {
        List<Users> users = userRepository.findAll();
        Comparator<Users> comparator = Comparator.comparing(
                "name".equals(field) ? Users::getName : Users::getEmail,
                Comparator.nullsLast(String::compareTo)
        );
        users.sort(comparator);
        return users;
    }

    public Map<String, List<Users>> groupByEmailDomain() {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().contains("@"))
                .collect(Collectors.groupingBy(u -> u.getEmail().split("@")[1]));
    }

    public List<Users> searchUsersByName(String keyword) {
        return userRepository.findAll().stream()
                .filter(u -> Optional.ofNullable(u.getName())
                        .map(name -> name.toLowerCase().contains(keyword.toLowerCase())).orElse(false))
                .collect(Collectors.toList());
    }
}
