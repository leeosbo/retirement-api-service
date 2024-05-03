package com.retirement.apiservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retirement.apiservice.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findAllByUserId(int UserId);

    Expense findByIdAndUserId(int id, int UserId);
}
