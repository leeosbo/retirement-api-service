package com.retirement.apiservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.Goal;
import com.retirement.apiservice.repository.GoalRepository;

@Service
public class GoalService {
    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public List<Goal> getAllGoals(int requestUserId, CustomUser authenticatedUser) {
        boolean authorized = userIsAuthorized(requestUserId, authenticatedUser);

        if (!authorized) {
            return null;
        }

        return goalRepository.findAllByUserId(requestUserId);
    }

    private boolean userIsAuthorized(int requestUserId, CustomUser authenticatedUser) {
        boolean authorized = false;
        int authenticatedUserId = authenticatedUser.getUserId();

        if (requestUserId == authenticatedUserId) {
            authorized = true;
        }

        return authorized;
    }

    public Goal getGoal(int goalId, CustomUser authenticatedUser) {
        return goalRepository.findByIdAndUserId(goalId, authenticatedUser.getUserId());
    }

    public Goal create(Goal goal, CustomUser authenticatedUser) {
        if (userIsAuthorized(goal.getUserId(), authenticatedUser)) {
            return goalRepository.save(goal);
        }
        return null;
    }

    public boolean update(Goal goal) {
        Optional<Goal> retrievedGoal = Optional
                .ofNullable(goalRepository.findByIdAndUserId(goal.getId(), goal.getUserId()));

        if (retrievedGoal.isPresent()) {
            goalRepository.save(goal);
            return true;
        }

        return false;
    }

    public boolean delete(Goal goal) {
        Optional<Goal> retrievedGoal = Optional
                .ofNullable(goalRepository.findByIdAndUserId(goal.getId(), goal.getUserId()));

        if (retrievedGoal.isPresent()) {
            goalRepository.deleteById(goal.getId());
            return true;
        }

        return false;
    }
}
