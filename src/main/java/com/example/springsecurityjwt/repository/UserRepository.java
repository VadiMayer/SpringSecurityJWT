package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepository {
    private CrudUserRepository crudUserRepository;

    public User get(long userId) {
        return crudUserRepository.findById(userId).orElse(null);
    }
    public User get(String email) {
        return crudUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User create(User user) {
        return crudUserRepository.save(user);
    }
    public int size() {
        return crudUserRepository.findAll().size();
    }
}
