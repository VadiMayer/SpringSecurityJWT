package com.example.springsecurityjwt.config;

import com.example.springsecurityjwt.security.JwtAuthenticationFilter;
import com.example.springsecurityjwt.security.JwtAuthenticationProvider;
import com.example.springsecurityjwt.service.UserService;
import com.example.springsecurityjwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends AbstractHttpConfigurer<SecurityConfiguration, HttpSecurity> {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(new JwtAuthenticationProvider(jwtUtil, userService.userDetailsService()))
                .authorizeHttpRequests(request -> request.requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .permitAll());
    }
}
