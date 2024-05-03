package com.retirement.apiservice.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retirement.apiservice.entity.User;
import com.retirement.apiservice.repository.UserRepository;

@RestController
@RequestMapping("/retirement/api/users")
public class UserController {
    private final UserRepository repository;

    private UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{userId}")
    private ResponseEntity<User> getUser(@PathVariable int userId, Principal principal) {
        Optional<User> userOptional = Optional.ofNullable(repository.findByUserId(userId));
        if (userOptional.isPresent() && userOptional.get().getEmail() == principal.getName()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
