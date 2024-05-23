package com.retirement.apiservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retirement.apiservice.entity.Estimate;
import com.retirement.apiservice.service.CustomUser;
import com.retirement.apiservice.service.EstimateService;

@RestController
@RequestMapping("/retirement/api/estimates")
public class EstimateController {
    @Autowired
    private EstimateService estimateService;

    @GetMapping("/{userId}")
    private ResponseEntity<Estimate> getRetirementEstimate(@PathVariable int userId, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        Optional<Estimate> retirementEstimateOptional = Optional
                .ofNullable(estimateService.getEstimate(userId, authenticatedUser));
        if (retirementEstimateOptional.isPresent())
            return ResponseEntity.ok(retirementEstimateOptional.get());
        return ResponseEntity.notFound().build();
    }
}
