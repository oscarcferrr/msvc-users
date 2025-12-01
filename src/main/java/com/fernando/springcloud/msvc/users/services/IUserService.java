package com.fernando.springcloud.msvc.users.services;

import java.util.Optional;

import com.fernando.springcloud.msvc.users.entities.User;

public interface IUserService {
    Iterable<User> findAll();

    Optional<User> findById(Long id);

    Optional<User>  findByUsername(String username);

    User save(User user);

    void delete(Long id);

    Optional<User> update(User user, Long id);
}
