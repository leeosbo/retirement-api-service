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
    private ResponseEntity<List<Goal>> getAllGoals(@RequestParam int user, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<List<Goal>> goal = Optional
                .ofNullable(goalService.getAllGoals(user, principalDetails));

        if (goal.isPresent()) {
            return ResponseEntity.ok(goal.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<Goal> getGoal(@PathVariable int id, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<Goal> Goal = Optional
                .ofNullable(goalService.getGoal(id, principalDetails));

        if (Goal.isPresent()) {
            return ResponseEntity.ok(Goal.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<String> postGoal(@Valid @RequestBody Goal goal,
            UriComponentsBuilder uriComponentsBuilder, Authentication auth) {

        CustomUser principalDetails = (CustomUser) auth.getPrincipal();
        Goal createdGoal = goalService.create(goal, principalDetails);

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
        boolean successfulUpdate = goalService.update(validatedGoal);

        if (successfulUpdate) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{goalId}")
    private ResponseEntity<Void> deleteGoal(@PathVariable int goalId, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();
        Goal validatedGoal = goalValidator.validated(goalId, new Goal(), principalDetails.getUserId());
        boolean successfulDeletion = goalService.delete(validatedGoal);

        if (successfulDeletion) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
