package com.retirement.apiservice.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.Authority;
import com.retirement.apiservice.entity.User;
import com.retirement.apiservice.repository.AuthorityRepository;
import com.retirement.apiservice.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthorityRepository authRepository;

    public CustomUserDetailsService(UserRepository userRepository, AuthorityRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username));
        if (user.isPresent()) {
            Optional<Authority> authority = Optional.ofNullable(authRepository.findByUserId(user.get().getUserId()));
            if (authority.isPresent())
                return new CustomUser(user.get(), authority.get());
            return new CustomUser(user.get());
        } else {
            throw new UsernameNotFoundException("user not found: " + username);
        }
    }

}
