package com.example.demo.controller;

import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestParam String username, @RequestParam String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(username);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.badRequest().body("Authentication failed");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtUtil.isTokenValid(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/extract-username")
    public ResponseEntity<String> extractUsername(@RequestParam String token) {
        return jwtUtil.extractUsername(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Invalid token"));
    }
}