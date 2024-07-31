package com.example.springsecurityjwt.repository;

import com.example.springsecurityjwt.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepository {
    private CrudUserRepository crudUserRepository;

    public User get(long id) {
        return crudUserRepository.findById(id).orElse(null);
    }
}
