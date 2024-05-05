package com.retirement.apiservice.validator;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.IncomeSource;

@Service
public class IncomeSourceValidator implements IValidator<IncomeSource> {
    @Override
    public IncomeSource validated(int incomeSourceId, IncomeSource updatedIncomeSource, int authenticateUserId) {
        if (incomeSourceId != updatedIncomeSource.getId()) {
            updatedIncomeSource.setId(incomeSourceId);
        }

        if (updatedIncomeSource.getUserId() != authenticateUserId) {
            updatedIncomeSource.setUserId(authenticateUserId);
        }

        return updatedIncomeSource;
    }
}
