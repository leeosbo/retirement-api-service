package com.retirement.apiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retirement.apiservice.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    Authority findByUserId(int userId);
}
