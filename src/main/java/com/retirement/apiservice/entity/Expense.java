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
import jakarta.validation.constraints.PositiveOrZero;

@Entity(name = "expense")
public class Expense {
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

    @NotNull
    @PositiveOrZero
    private int amount;

    @Column(name = "yearly_frequency")
    @NotNull
    @PositiveOrZero
    private int frequencyPerYear;

    public Expense() {
    }

    public Expense(int id, int userId, String name, int amount, int frequencyPerYear) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.frequencyPerYear = frequencyPerYear;
    }

    public Expense(int userId, String name, int amount, int frequencyPerYear) {
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.frequencyPerYear = frequencyPerYear;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int newAmount) {
        this.amount = newAmount;
    }

    public int getFrequencyPerYear() {
        return frequencyPerYear;
    }

    public void setFrequencyPerYear(int newFrequency) {
        this.frequencyPerYear = newFrequency;
    }

}
