package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private static final List<Users> USERS = new ArrayList<Users>();
    private static long idCounter = 100;

    static {
        USERS.add(new Users(1L, "Alice", "alice@example.com"));
        USERS.add(new Users(2L, "Bob", "bob@test.com"));
        USERS.add(new Users(3L, "Charlie", "charlie@example.com"));
        USERS.add(new Users(4L, "David", "david@test.com"));
        USERS.add(new Users(5L, "Eva", "eva@example.com"));
    }

    public Users save(Users user) {
        if (user.getId() == null) {
            user.setId(++idCounter);
        }
        USERS.add(user);
        return user;
    }

    public List<Users> findAll() {
        return new ArrayList<Users>(USERS);
    }
}
