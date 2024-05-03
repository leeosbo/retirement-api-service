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

import com.retirement.apiservice.entity.IncomeSource;
import com.retirement.apiservice.repository.IncomeSourceRepository;
import com.retirement.apiservice.service.CustomUser;
import com.retirement.apiservice.service.IncomeSourceService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/retirement/api/incomesources")
public class IncomeSourceController {

    @Autowired
    private IncomeSourceRepository incomeSourceRepository;

    @Autowired
    private IncomeSourceService incomeSourceService;

    @GetMapping
    private ResponseEntity<List<IncomeSource>> getAllIncomeSources(@RequestParam int user, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<List<IncomeSource>> incomeSource = Optional
                .ofNullable(incomeSourceService.getAllIncomeSources(user, principalDetails));

        if (incomeSource.isPresent()) {
            return ResponseEntity.ok(incomeSource.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<IncomeSource> getIncomeSource(@PathVariable int id, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<IncomeSource> incomeSource = Optional
                .ofNullable(incomeSourceService.getIncomeSource(id, principalDetails));

        if (incomeSource.isPresent()) {
            return ResponseEntity.ok(incomeSource.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<String> postIncomeSource(@Valid @RequestBody IncomeSource incomeSource,
            UriComponentsBuilder uriComponentsBuilder, Authentication auth) {

        CustomUser principalDetails = (CustomUser) auth.getPrincipal();
        IncomeSource createdIncomeSource = incomeSourceService.create(incomeSource, principalDetails);

        if (createdIncomeSource != null) {
            URI createdIncomeSourceLocation = uriComponentsBuilder
                    .path("retirement/api/incomesources/{id}")
                    .buildAndExpand(createdIncomeSource.getId())
                    .toUri();
            return ResponseEntity.created(createdIncomeSourceLocation).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<String> putIncomeSource(@PathVariable int id,
            @Valid @RequestBody IncomeSource updatedIncomeSource,
            Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();

        Optional<IncomeSource> incomeSource = Optional
                .ofNullable(incomeSourceRepository.findByIdAndUserId(id, principalDetails.getUserId()));

        if (!incomeSource.isPresent() || updatedIncomeSource.getUserId() != incomeSource.get().getUserId()) {
            return ResponseEntity.notFound().build();
        }

        IncomeSource updated = new IncomeSource(id, principalDetails.getUserId(), updatedIncomeSource.getName(),
                updatedIncomeSource.getAccountBalance(), updatedIncomeSource.getReturnRate(),
                updatedIncomeSource.getReturnFrequency());

        incomeSourceRepository.save(updated);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteIncomeSource(@PathVariable int id, Authentication auth) {
        CustomUser principalDetails = (CustomUser) auth.getPrincipal();
        Optional<IncomeSource> incomeSource = Optional
                .ofNullable(incomeSourceRepository.findByIdAndUserId(id, principalDetails.getUserId()));
        if (!incomeSource.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        incomeSourceRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
