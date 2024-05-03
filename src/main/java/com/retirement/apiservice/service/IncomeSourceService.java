package com.retirement.apiservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.IncomeSource;
import com.retirement.apiservice.repository.IncomeSourceRepository;

@Service
public class IncomeSourceService {
    private final IncomeSourceRepository incomeSourceRepository;

    public IncomeSourceService(IncomeSourceRepository incomeSourceRepository) {
        this.incomeSourceRepository = incomeSourceRepository;
    }

    public List<IncomeSource> getAllIncomeSources(int requestUserId, CustomUser authenticatedUser) {
        boolean authorized = userIsAuthorized(requestUserId, authenticatedUser);

        if (!authorized) {
            return null;
        }

        return incomeSourceRepository.findAllByUserId(requestUserId);
    }

    private boolean userIsAuthorized(int requestUserId, CustomUser authenticatedUser) {
        boolean authorized = false;
        int authenticatedUserId = authenticatedUser.getUserId();

        if (requestUserId == authenticatedUserId) {
            authorized = true;
        }

        return authorized;
    }

    public IncomeSource getIncomeSource(int incomeSourceId, CustomUser authenticatedUser) {
        return incomeSourceRepository.findByIdAndUserId(incomeSourceId, authenticatedUser.getUserId());
    }

    public IncomeSource create(IncomeSource incomeSource, CustomUser authenticatedUser) {
        if (userIsAuthorized(incomeSource.getUserId(), authenticatedUser)) {
            return incomeSourceRepository.save(incomeSource);
        }
        return null;
    }
}