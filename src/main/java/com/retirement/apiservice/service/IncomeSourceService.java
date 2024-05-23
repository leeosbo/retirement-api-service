package com.retirement.apiservice.service;

import java.util.List;
import java.util.Optional;

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
        if (authenticatedUser.isAuthorizedToAccessUserResources(requestUserId))
            return incomeSourceRepository.findAllByUserId(requestUserId);
        return null;
    }

    public IncomeSource getIncomeSource(int incomeSourceId, CustomUser authenticatedUser) {
        return incomeSourceRepository.findByIdAndUserId(incomeSourceId, authenticatedUser.getUserId());
    }

    public IncomeSource create(IncomeSource incomeSource, CustomUser authenticatedUser) {
        if (authenticatedUser.isAuthorizedToAccessUserResources(incomeSource.getUserId()))
            return incomeSourceRepository.save(incomeSource);
        return null;
    }

    public boolean update(IncomeSource incomeSource) {
        Optional<IncomeSource> retrievedIncomeSource = Optional
                .ofNullable(incomeSourceRepository.findByIdAndUserId(incomeSource.getId(), incomeSource.getUserId()));
        if (retrievedIncomeSource.isPresent()) {
            incomeSourceRepository.save(incomeSource);
            return true;
        }
        return false;
    }

    public boolean delete(IncomeSource incomeSource) {
        Optional<IncomeSource> retrievedIncomeSource = Optional
                .ofNullable(incomeSourceRepository.findByIdAndUserId(incomeSource.getId(), incomeSource.getUserId()));
        if (retrievedIncomeSource.isPresent()) {
            incomeSourceRepository.deleteById(incomeSource.getId());
            return true;
        }
        return false;
    }
}
