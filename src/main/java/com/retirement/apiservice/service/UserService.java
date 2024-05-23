package com.retirement.apiservice.service;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.User;
import com.retirement.apiservice.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(int userId, CustomUser authenticatedUser) {
        if (authenticatedUser.isAuthorizedToAccessUserResources(userId))
            return userRepository.findByUserId(userId);
        return null;
    }
}
