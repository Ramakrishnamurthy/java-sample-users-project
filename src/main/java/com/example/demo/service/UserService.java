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
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmailOptional()
                        .map(email -> email.endsWith("@" + domain))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    public List<Users> sortUsersBy(final String field) {
        return userRepository.findAll()
                .stream()
                .sorted((u1, u2) -> {
                    switch (field) {
                        case "name":
                            return u1.getNameOptional()
                                    .orElse("")
                                    .compareTo(u2.getNameOptional().orElse(""));
                        case "email":
                            return u1.getEmailOptional()
                                    .orElse("")
                                    .compareTo(u2.getEmailOptional().orElse(""));
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    public Map<String, List<Users>> groupByEmailDomain() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmailOptional().isPresent())
                .collect(Collectors.groupingBy(user -> {
                    String email = user.getEmailOptional().get();
                    String[] parts = email.split("@");
                    return parts.length == 2 ? parts[1] : "unknown";
                }));
    }

    public List<Users> searchUsersByName(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getNameOptional()
                        .map(name -> name.toLowerCase().contains(lowerKeyword))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    public Optional<Users> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Users> findUsersByEmailPattern(String pattern) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmailOptional()
                        .map(email -> email.matches(pattern))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getEmailDomainCounts() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmailOptional().isPresent())
                .collect(Collectors.groupingBy(user -> {
                    String email = user.getEmailOptional().get();
                    String[] parts = email.split("@");
                    return parts.length == 2 ? parts[1] : "unknown";
                }, Collectors.counting()));
    }
}
