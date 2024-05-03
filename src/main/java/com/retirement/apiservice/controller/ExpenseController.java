package com.retirement.apiservice.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.retirement.apiservice.entity.Expense;
import com.retirement.apiservice.service.CustomUser;
import com.retirement.apiservice.service.ExpenseService;
import com.retirement.apiservice.validator.ExpenseValidator;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/retirement/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseValidator expenseValidator;

    @GetMapping
    private ResponseEntity<List<Expense>> getAllExpenses(@RequestParam int user, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<List<Expense>> expense = Optional
                .ofNullable(expenseService.getAllExpenses(user, principalDetails));

        if (expense.isPresent()) {
            return ResponseEntity.ok(expense.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Expense> getExpense(@PathVariable int id, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<Expense> expense = Optional
                .ofNullable(expenseService.getExpense(id, principalDetails));

        if (expense.isPresent()) {
            return ResponseEntity.ok(expense.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<String> postExpense(@Valid @RequestBody Expense expense,
            UriComponentsBuilder uriComponentsBuilder, Authentication auth) {

        CustomUser principalDetails = (CustomUser) auth.getPrincipal();
        Expense createdExpense = expenseService.create(expense, principalDetails);

        if (createdExpense != null) {
            URI createdExpenseLocation = uriComponentsBuilder
                    .path("retirement/api/expenses/{id}")
                    .buildAndExpand(createdExpense.getId())
                    .toUri();
            return ResponseEntity.created(createdExpenseLocation).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{expenseId}")
    private ResponseEntity<String> putExpense(@PathVariable int expenseId, @Valid @RequestBody Expense updatedExpense,
            Authentication auth) {

        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Expense validatedExpense = expenseValidator.validated(expenseId, updatedExpense, authenticatedUser.getUserId());
        boolean successfulUpdate = expenseService.update(validatedExpense);

        if (successfulUpdate) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{expenseId}")
    private ResponseEntity<Void> deleteExpense(@PathVariable int expenseId, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();
        Expense validatedExpense = expenseValidator.validated(expenseId, new Expense(), principalDetails.getUserId());
        boolean successfulDeletion = expenseService.delete(validatedExpense);

        if (successfulDeletion) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
