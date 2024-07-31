package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User get(long id) {
        return userRepository.get(id);
    }
}
