package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrudUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
