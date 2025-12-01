package com.fernando.springcloud.msvc.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernando.springcloud.msvc.users.entities.Role;
import com.fernando.springcloud.msvc.users.entities.User;
import com.fernando.springcloud.msvc.users.repositories.RoleRepository;
import com.fernando.springcloud.msvc.users.repositories.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setEnabled(true);
        user.setRoles(getRoleOptional(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(User user, Long id) {

        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(userDb -> {
            userDb.setUsername(user.getUsername());
            userDb.setPassword(passwordEncoder.encode(user.getPassword()));
            userDb.setEmail(user.getEmail());

            if (user.isEnabled() != null) {
                userDb.setEnabled(user.isEnabled());
            }else{
                userDb.setEnabled(true);
            }
            
            userDb.setRoles(getRoleOptional(user));
            return Optional.of(userRepository.save(userDb));

        }).orElseGet(() -> Optional.empty());

    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    private List<Role> getRoleOptional(User user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(role -> roles.add(role));

        if(user.isAdmin()){
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(role -> roles.add(role));
        }
        return roles;
    }

}
