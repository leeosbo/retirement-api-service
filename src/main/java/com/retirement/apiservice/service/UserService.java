package com.retirement.apiservice.service;

import com.retirement.apiservice.entity.User;
import com.retirement.apiservice.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(int userId) {
        return userRepository.findByUserId(userId);
    }
}
