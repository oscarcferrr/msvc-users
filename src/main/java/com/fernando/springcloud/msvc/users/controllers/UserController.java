package com.fernando.springcloud.msvc.users.controllers;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fernando.springcloud.msvc.users.entities.User;
import com.fernando.springcloud.msvc.users.services.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService service;

    private PasswordEncoder passwordEncoder;

    public UserController(IUserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        User user = service.findById(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }


    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String usrname) {
        User user = service.findByUsername(usrname);
        if (user == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

         user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors());

        User userDb = service.findById(id);
        if (userDb == null)
            return ResponseEntity.notFound().build();


        userDb.setUsername(user.getUsername());
        userDb.setPassword(user.getPassword());
        userDb.setEmail(user.getEmail());
        if(user.isEnabled() != null)
             userDb.setEnabled(user.isEnabled());

        return ResponseEntity.ok(service.save(userDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User userDb = service.findById(id);
        if (userDb == null)
            return ResponseEntity.notFound().build();

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}