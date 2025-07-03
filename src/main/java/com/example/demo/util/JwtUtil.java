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
        Instant expiration = now.plus(10, ChronoUnit.HOURS);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Optional<String> extractUsername(String token) {
        return getClaims(token)
                .map(Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        return getClaims(token)
                .map(claims -> !isTokenExpired(claims))
                .orElse(false);
    }

    private Optional<Claims> getClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(Date.from(Instant.now()));
    }
}