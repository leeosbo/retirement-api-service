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

    public Optional<Goal> getPrimaryGoal(int requestUserId, CustomUser authenticatedUser) {
        List<Goal> goalList = getAllGoals(requestUserId, authenticatedUser);
        Optional<Goal> primaryGoal = null;
        primaryGoal = goalList.stream().filter(goal -> goal.getPrimaryGoal() == true).findFirst();
        return primaryGoal;
    }

    public List<Goal> getAllGoals(int requestUserId, CustomUser authenticatedUser) {
        if (authenticatedUser.isAuthorizedToAccessUserResources(requestUserId))
            return goalRepository.findAllByUserId(requestUserId);
        return null;
    }

    public Goal getGoal(int goalId, CustomUser authenticatedUser) {
        return goalRepository.findByIdAndUserId(goalId, authenticatedUser.getUserId());
    }

    public Goal create(Goal goal, CustomUser authenticatedUser) {
        if (authenticatedUser.isAuthorizedToAccessUserResources(goal.getUserId()))
            return goalRepository.save(goal);
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
