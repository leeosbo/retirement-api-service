package com.retirement.apiservice.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retirement.apiservice.entity.Retiree;
import com.retirement.apiservice.repository.RetireeRepository;

@RestController
@RequestMapping("/retirement/api/retirees")
public class RetireeController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RetireeRepository repository;

    @GetMapping("/{userId}")
    private ResponseEntity<Retiree> getUser(@PathVariable int userId, Principal principal) {

        Optional<Retiree> retireeOptional = Optional.ofNullable(repository.findByUserId(userId));
        if (retireeOptional.isPresent() && retireeOptional.get().getEmail() == principal.getName()) {
            return ResponseEntity.ok(retireeOptional.get());
        } else {
            log.info("User not found: " + userId);
            return ResponseEntity.notFound().build();
        }
    }
}
