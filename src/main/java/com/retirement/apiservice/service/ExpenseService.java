package com.retirement.apiservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.Expense;
import com.retirement.apiservice.repository.ExpenseRepository;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses(int requestUserId, CustomUser authenticatedUser) {
        if (authenticatedUser.isAuthorizedToAccessUserResources(requestUserId))
            return expenseRepository.findAllByUserId(requestUserId);
        return null;
    }

    public Expense getExpense(int expenseId, CustomUser authenticatedUser) {
        return expenseRepository.findByIdAndUserId(expenseId, authenticatedUser.getUserId());
    }

    public Expense create(Expense expense, CustomUser authenticatedUser) {
        if (authenticatedUser.isAuthorizedToAccessUserResources(expense.getUserId()))
            return expenseRepository.save(expense);
        return null;
    }

    public boolean update(Expense expense) {
        Optional<Expense> retrievedExpense = Optional
                .ofNullable(expenseRepository.findByIdAndUserId(expense.getId(), expense.getUserId()));
        if (retrievedExpense.isPresent()) {
            expenseRepository.save(expense);
            return true;
        }
        return false;
    }

    public boolean delete(Expense expense) {
        Optional<Expense> retrievedExpense = Optional
                .ofNullable(expenseRepository.findByIdAndUserId(expense.getId(), expense.getUserId()));
        if (retrievedExpense.isPresent()) {
            expenseRepository.deleteById(expense.getId());
            return true;
        }
        return false;
    }
}
