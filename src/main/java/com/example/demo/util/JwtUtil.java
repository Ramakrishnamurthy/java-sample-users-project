package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "a9f8b7c6d5e4g3h2i1j0k9l8m7n6o5p4q3r2s1t0u9v8w7x6y5z4!@#";

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(10, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Optional.ofNullable(getClaims(token))
                .map(Claims::getSubject)
                .orElse("");
    }

    public boolean isTokenValid(String token) {
        return Optional.ofNullable(token)
                .filter(t -> !isTokenExpired(t))
                .isPresent();
    }

    private Claims getClaims(String token) {
        String secret = SECRET_KEY;
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return Optional.ofNullable(getClaims(token))
                .map(Claims::getExpiration)
                .map(exp -> exp.toInstant().isBefore(Instant.now()))
                .orElse(true);
    }
}