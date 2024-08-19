package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dto.SignUpRequest;
import com.example.springsecurityjwt.model.User;

public interface AuthenticationService {
    User signup(SignUpRequest signUpRequest);
}
