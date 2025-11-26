package com.fernando.springcloud.msvc.users.services;

import com.fernando.springcloud.msvc.users.entities.User;

public interface IUserService {
    Iterable<User> findAll();

    User findById(Long id);

    User findByUsername(String username);

    User save(User user);

    void delete(Long id);
}
