package com.retirement.apiservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retirement.apiservice.entity.IncomeSource;

public interface IncomeSourceRepository extends JpaRepository<IncomeSource, Integer> {
    List<IncomeSource> findAllByUserId(int UserId);

    IncomeSource findByIdAndUserId(int id, int UserId);
}
