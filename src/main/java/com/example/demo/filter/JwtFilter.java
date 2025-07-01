package com.example.demo.filter;

import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        Optional<String> tokenOpt = Optional.ofNullable(authorizationHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));

        Optional<String> usernameOpt = tokenOpt.map(jwtUtil::extractUsername);

        if (usernameOpt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = tokenOpt.get();
            if (jwtUtil.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(usernameOpt.get(), null, null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}