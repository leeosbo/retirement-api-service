package com.retirement.apiservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retirement.apiservice.entity.Goal;

public interface GoalRepository extends JpaRepository<Goal, Integer> {
    List<Goal> findAllByUserId(int UserId);

    Goal findByIdAndUserId(int goalId, int UserId);
}
