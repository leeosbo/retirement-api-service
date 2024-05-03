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
        boolean authorized = userIsAuthorized(requestUserId, authenticatedUser);

        if (!authorized) {
            return null;
        }

        return expenseRepository.findAllByUserId(requestUserId);
    }

    private boolean userIsAuthorized(int requestUserId, CustomUser authenticatedUser) {
        boolean authorized = false;
        int authenticatedUserId = authenticatedUser.getUserId();

        if (requestUserId == authenticatedUserId) {
            authorized = true;
        }

        return authorized;
    }

    public Expense getExpense(int expenseId, CustomUser authenticatedUser) {
        return expenseRepository.findByIdAndUserId(expenseId, authenticatedUser.getUserId());
    }

    public Expense create(Expense expense, CustomUser authenticatedUser) {
        if (userIsAuthorized(expense.getUserId(), authenticatedUser)) {
            return expenseRepository.save(expense);
        }
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
