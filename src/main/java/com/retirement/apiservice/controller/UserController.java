package com.retirement.apiservice.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retirement.apiservice.entity.User;
import com.retirement.apiservice.service.CustomUser;
import com.retirement.apiservice.service.UserService;

@RestController
@RequestMapping("/retirement/api/users")
public class UserController {
    @Autowired
    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    private ResponseEntity<User> getUser(@PathVariable int userId, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Optional<User> userOptional = Optional.ofNullable(userService.getUser(userId, authenticatedUser));
        if (userOptional.isPresent())
            return ResponseEntity.ok(userOptional.get());
        return ResponseEntity.notFound().build();
    }
}
