package com.retirement.apiservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity(name = "goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    @JsonProperty("userId")
    @NotNull
    private int userId;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[^0-9]*", message = "value must be string/text")
    private String name;

    @Column(name = "disposable_income")
    @JsonProperty("disposableIncome")
    @NotNull
    @Positive
    private int disposableIncome;

    @Column(name = "yearly_frequency")
    @NotNull
    @Positive
    private int frequency;

    @Column(name = "primary_goal")
    @JsonProperty("primaryGoal")
    @NotNull
    private boolean primaryGoal;

    public Goal() {
    }

    public Goal(int id, int userId, String name, int disposableIncome, int frequency, boolean primaryGoal) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.disposableIncome = disposableIncome;
        this.frequency = frequency;
        this.primaryGoal = primaryGoal;
    }

    public Goal(int userId, String name, int disposableIncome, int frequency, boolean primaryGoal) {
        this.userId = userId;
        this.name = name;
        this.disposableIncome = disposableIncome;
        this.frequency = frequency;
        this.primaryGoal = primaryGoal;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDisposableIncome() {
        return disposableIncome;
    }

    public void setDisposableIncome(int newIncomeGoal) {
        this.disposableIncome = newIncomeGoal;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int newFrequency) {
        this.frequency = newFrequency;
    }

    public boolean getPrimaryGoal() {
        return primaryGoal;
    }

    public void setPrimaryGoal(boolean isPrimary) {
        primaryGoal = isPrimary;
    }
}
