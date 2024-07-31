package com.example.springsecurityjwt.security;

import com.example.springsecurityjwt.service.UserService;
import com.example.springsecurityjwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null && validateToken(token)) {
            Authentication authentication = createAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Логика извлечения токена из запроса (например, из заголовка Authorization)
        final String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }

    private boolean validateToken(String token) {
        // Логика верификации токена
    }

    private Authentication createAuthentication(String token) {
        // Логика создания объекта Authentication на основе токена

        // Получение информации о пользователе из токена
        UserDetails userDetails = extractUserDetailsFromToken(token);

        // Создание объекта Authentication
        return new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private UserDetails extractUserDetailsFromToken(String token) {
        // Логика извлечения информации о пользователе из токена
        final String userEmail = jwtUtil.extractUserName(token);
    }
}
