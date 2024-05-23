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

import com.retirement.apiservice.entity.Goal;
import com.retirement.apiservice.service.CustomUser;
import com.retirement.apiservice.service.GoalService;
import com.retirement.apiservice.validator.GoalValidator;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/retirement/api/goals")
public class GoalController {
    @Autowired
    private GoalService goalService;
    @Autowired
    private GoalValidator goalValidator;

    @GetMapping
    private ResponseEntity<List<Goal>> getAllGoals(@RequestParam int userId, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Optional<List<Goal>> goal = Optional
                .ofNullable(goalService.getAllGoals(userId, authenticatedUser));
        if (goal.isPresent())
            return ResponseEntity.ok(goal.get());
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{goalId}")
    private ResponseEntity<Goal> getGoal(@PathVariable int goalId, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Optional<Goal> Goal = Optional
                .ofNullable(goalService.getGoal(goalId, authenticatedUser));
        if (Goal.isPresent())
            return ResponseEntity.ok(Goal.get());
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<String> postGoal(@Valid @RequestBody Goal goal,
            UriComponentsBuilder uriComponentsBuilder, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Goal createdGoal = goalService.create(goal, authenticatedUser);
        if (createdGoal != null) {
            URI createdGoalLocation = uriComponentsBuilder
                    .path("retirement/api/goals/{id}")
                    .buildAndExpand(createdGoal.getId())
                    .toUri();
            return ResponseEntity.created(createdGoalLocation).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{goalId}")
    private ResponseEntity<String> putGoal(@PathVariable int goalId, @Valid @RequestBody Goal updatedGoal,
            Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Goal validatedGoal = goalValidator.validated(goalId, updatedGoal, authenticatedUser.getUserId());
        boolean updateIsSuccessful = goalService.update(validatedGoal);
        if (updateIsSuccessful)
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{goalId}")
    private ResponseEntity<Void> deleteGoal(@PathVariable int goalId, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Goal validatedGoal = goalValidator.validated(goalId, new Goal(), authenticatedUser.getUserId());
        boolean deleteIsSuccessful = goalService.delete(validatedGoal);
        if (deleteIsSuccessful)
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
