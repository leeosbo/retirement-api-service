package com.retirement.apiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retirement.apiservice.entity.Retiree;

@Repository
public interface RetireeRepository extends JpaRepository<Retiree, Integer> {
    Retiree findByUserId(int userId);
}
