package com.retirement.apiservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retirement.apiservice.service.CustomUser;

@RestController
public class LoginController {

    @PostMapping("/login")
    private ResponseEntity<Integer> login(Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        return ResponseEntity.ok(user.getUserId());
    }
}
