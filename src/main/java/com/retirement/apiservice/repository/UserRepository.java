package com.retirement.apiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retirement.apiservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(int userId);

    User findByEmail(String email);
}
