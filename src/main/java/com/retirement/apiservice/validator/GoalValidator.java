package com.retirement.apiservice.validator;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.Goal;

@Service
public class GoalValidator implements IValidator<Goal> {
    @Override
    public Goal validated(int goalId, Goal updatedGoal, int authenticateUserId) {
        // mismatch of goalId comparing values from parameter and request body
        if (goalId != updatedGoal.getId()) {
            // the goal id parameter takes precedence
            updatedGoal.setId(goalId);
        }

        // mismatch of userId value from request body and principal's userId
        if (updatedGoal.getUserId() != authenticateUserId) {
            updatedGoal.setUserId(authenticateUserId);
        }

        return updatedGoal;
    }
}
