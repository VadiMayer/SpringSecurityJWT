package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.dto.SignUpRequest;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.AuthenticationService;
import com.example.springsecurityjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    @GetMapping
    public UserDetails get(@RequestBody User user) {
        return userService.userDetailsService().loadUserByUsername(user.getUsername());
    }

    @PostMapping("/signup")
    public ResponseEntity<User> singUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }
}
