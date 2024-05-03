package com.retirement.apiservice.validator;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.Expense;

@Service
public class ExpenseValidator implements IValidator<Expense> {
    @Override
    public Expense validated(int expenseId, Expense updatedExpense, int authenticateUserId) {
        if (expenseId != updatedExpense.getId()) {
            updatedExpense.setId(expenseId);
        }

        if (updatedExpense.getUserId() != authenticateUserId) {
            updatedExpense.setUserId(authenticateUserId);
        }

        return updatedExpense;
    }
}
