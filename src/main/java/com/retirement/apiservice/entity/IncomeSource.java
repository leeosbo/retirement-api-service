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

@Entity(name = "income_source")
public class IncomeSource {
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

    @Column(name = "account_balance")
    @JsonProperty("accountBalance")
    @NotNull
    @PositiveOrZero
    private int accountBalance;

    @Column(name = "return_rate")
    @JsonProperty("returnRate")
    @NotNull
    @PositiveOrZero
    private double returnRate;

    @Column(name = "return_frequency")
    @NotNull
    @PositiveOrZero
    private int returnFrequency; // per year

    protected IncomeSource() {
    }

    public IncomeSource(int id, int userId, String name, int accountBalance, double returnRate, int returnFrequency) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.accountBalance = accountBalance;
        this.returnRate = returnRate;
        this.returnFrequency = returnFrequency;
    }

    public IncomeSource(int userId, String name, int accountBalance, double returnRate, int returnFrequency) {
        this.userId = userId;
        this.name = name;
        this.accountBalance = accountBalance;
        this.returnRate = returnRate;
        this.returnFrequency = returnFrequency;
    }

    public int getId() {
        return id;
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

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(double returnRate) {
        this.returnRate = returnRate;
    }

    public int getReturnFrequency() {
        return returnFrequency;
    }

    public void setReturnFrequency(int returnFrequency) {
        this.returnFrequency = returnFrequency;
    }

}
