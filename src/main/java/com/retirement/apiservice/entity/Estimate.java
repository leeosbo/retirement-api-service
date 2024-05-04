package com.retirement.apiservice.entity;

public record Estimate(int userId, int monthlyIncomeAvailable, int monthlyExpenses, int monthlyDisposable,
        boolean onTrack, int monthlyToSave, int totalAdditionalSavings) {
}
