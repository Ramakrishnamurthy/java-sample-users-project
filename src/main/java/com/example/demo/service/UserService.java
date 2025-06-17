
package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllUsers() {
        List<Users> result = new ArrayList<Users>();
        Iterable<Users> all = userRepository.findAll();
        Iterator<Users> iterator = all.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }


    public void addUser(Users users) {
        userRepository.save(users);
    }


    public List<Users> getUsersByDomain(String domain) {
        List<Users> result = new ArrayList<Users>();
        List<Users> all = userRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Users u = all.get(i);
            if (u.getEmail() != null && u.getEmail().endsWith("@" + domain)) {
                result.add(u);
            }
        }
        return result;
    }

    public List<Users> sortUsersBy(String field) {
        List<Users> users = userRepository.findAll();
        Collections.sort(users, new Comparator<Users>() {
            public int compare(Users u1, Users u2) {
                if ("name".equals(field)) {
                    return u1.getName().compareTo(u2.getName());
                } else if ("email".equals(field)) {
                    return u1.getEmail().compareTo(u2.getEmail());
                }
                return 0;
            }
        });
        return users;
    }

    public Map<String, List<Users>> groupByEmailDomain() {
        Map<String, List<Users>> map = new HashMap<String, List<Users>>();
        List<Users> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            Users u = users.get(i);
            String email = u.getEmail();
            if (email != null) {
                String[] parts = email.split("@");
                if (parts.length == 2) {
                    String domain = parts[1];
                    if (!map.containsKey(domain)) {
                        map.put(domain, new ArrayList<Users>());
                    }
                    map.get(domain).add(u);
                }
            }
        }
        return map;
    }

    public List<Users> searchUsersByName(String keyword) {
        List<Users> result = new ArrayList<Users>();
        List<Users> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            Users u = users.get(i);
            if (u.getName() != null && u.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(u);
            }
        }
        return result;
    }
}
