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
import com.retirement.apiservice.service.CustomUser;
import com.retirement.apiservice.service.IncomeSourceService;
import com.retirement.apiservice.validator.IncomeSourceValidator;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/retirement/api/incomesources")
public class IncomeSourceController {

    @Autowired
    private IncomeSourceService incomeSourceService;

    @Autowired
    private IncomeSourceValidator incomeSourceValidator;

    @GetMapping
    private ResponseEntity<List<IncomeSource>> getAllIncomeSources(@RequestParam int user, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();

        Optional<List<IncomeSource>> incomeSource = Optional
                .ofNullable(incomeSourceService.getAllIncomeSources(user, authenticatedUser));

        if (incomeSource.isPresent()) {
            return ResponseEntity.ok(incomeSource.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<IncomeSource> getIncomeSource(@PathVariable int id, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();

        Optional<IncomeSource> incomeSource = Optional
                .ofNullable(incomeSourceService.getIncomeSource(id, authenticatedUser));

        if (incomeSource.isPresent()) {
            return ResponseEntity.ok(incomeSource.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<String> postIncomeSource(@Valid @RequestBody IncomeSource incomeSource,
            UriComponentsBuilder uriComponentsBuilder, Authentication auth) {

        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        IncomeSource createdIncomeSource = incomeSourceService.create(incomeSource, authenticatedUser);

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

    @PutMapping("/{incomeSourceId}")
    private ResponseEntity<String> putIncomeSource(@PathVariable int incomeSourceId,
            @Valid @RequestBody IncomeSource updatedIncomeSource,
            Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        IncomeSource validatedIncomeSource = incomeSourceValidator.validated(incomeSourceId, updatedIncomeSource,
                authenticatedUser.getUserId());
        boolean successfulUpdate = incomeSourceService.update(validatedIncomeSource);

        if (successfulUpdate) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{incomeSourceId}")
    private ResponseEntity<Void> deleteIncomeSource(@PathVariable int incomeSourceId, Authentication auth) {
        CustomUser authenticatedUser = (CustomUser) auth.getPrincipal();
        IncomeSource validatedIncomeSource = incomeSourceValidator.validated(incomeSourceId, new IncomeSource(),
                authenticatedUser.getUserId());
        boolean successfulDeletion = incomeSourceService.delete(validatedIncomeSource);

        if (successfulDeletion) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
