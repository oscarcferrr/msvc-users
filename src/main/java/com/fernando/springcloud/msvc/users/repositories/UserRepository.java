package com.fernando.springcloud.msvc.users.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fernando.springcloud.msvc.users.entities.User;

public interface UserRepository  extends CrudRepository<User,Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User findByUsername(String username);
    
}
