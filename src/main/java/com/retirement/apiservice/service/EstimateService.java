package com.retirement.apiservice.service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.retirement.apiservice.entity.Estimate;
import com.retirement.apiservice.entity.Expense;
import com.retirement.apiservice.entity.Goal;
import com.retirement.apiservice.entity.IncomeSource;
import com.retirement.apiservice.entity.Retiree;
import com.retirement.apiservice.repository.RetireeRepository;

@Service
public class EstimateService {
    private final RetireeRepository retireeRepository;
    private final IncomeSourceService incomeSourceService;
    private final ExpenseService expenseService;
    private final GoalService goalService;

    public EstimateService(RetireeRepository retireeRepository, IncomeSourceService incomeSourceService,
            ExpenseService expenseService, GoalService goalService) {
        this.retireeRepository = retireeRepository;
        this.incomeSourceService = incomeSourceService;
        this.expenseService = expenseService;
        this.goalService = goalService;
    }

    public Estimate getEstimate(int userId, CustomUser authenticatedUser) {
        if (!authenticatedUser.isAuthorizedToAccessUserResources(userId))
            return null;
        Optional<Retiree> retireeOptional = Optional.ofNullable(retireeRepository.findByUserId(userId));
        if (!retireeOptional.isPresent())
            return null;
        // otherwise generate estimate
        // 1. calculate value of each income source at retirement date
        // 2. calculate monthly withdrawal potential for estimated number of retirement
        // years
        int monthlyIncomeAvailable = calculateMonthlyIncomeAvailable(retireeOptional.get(), authenticatedUser);
        // 3. calculate total monthly expenses
        int monthlyExpenses = calculateMonthlyExpenses(retireeOptional.get(), authenticatedUser);
        // 4. calculate monthly disposable income (result of step 2 minus 3)
        int monthlyDisposable = monthlyIncomeAvailable - monthlyExpenses;
        // add progress information
        boolean onTrack = true;
        int totalAdditionalSavings = 0;
        int monthlyToSave = 0;
        Optional<Goal> goalOptional = goalService.getPrimaryGoal(userId, authenticatedUser);
        if (goalOptional.isPresent()) {
            onTrack = monthlyDisposable - goalOptional.get().getMonthlyDisposableGoal() >= 0;
        }

        if (!onTrack) {
            totalAdditionalSavings = calculateAdditionalSavings(retireeOptional.get(), monthlyDisposable,
                    goalOptional.get().getMonthlyDisposableGoal());
            monthlyToSave = monthlyToSave(retireeOptional.get(), totalAdditionalSavings);
        }
        // 5. return estimate info in JSON format
        return new Estimate(userId, monthlyIncomeAvailable, monthlyExpenses, monthlyDisposable, onTrack, monthlyToSave,
                totalAdditionalSavings);
    }

    /**
     * Estimate available monthly income at retirement
     * 
     * @param retiree
     * @param authenticatedUser
     * @return int monthlyIncomeAvailable
     * @see https://search.credoreference.com/articles/Qm9va0FydGljbGU6Mzg0NzA1?aid=96753
     *      Time value of money
     * 
     */
    private int calculateMonthlyIncomeAvailable(Retiree retiree, CustomUser authenticatedUser) {
        List<IncomeSource> incomeSourceList = incomeSourceService.getAllIncomeSources(retiree.getUserId(),
                authenticatedUser);
        int totalIncomeAvailable = 0;
        double monthlyIncomeAvailable = 0;
        double yearsUntilRetirement = getYearsUntilRetirement(retiree);
        double retirementYears = retiree.getRetirementYears();
        if (yearsUntilRetirement < 0) {
            // retirement has already started, subtract from retirement years
            retirementYears = retirementYears + yearsUntilRetirement;
            yearsUntilRetirement = 1;
        }

        for (IncomeSource incomeSource : incomeSourceList) {
            totalIncomeAvailable += getFutureValue(incomeSource.getAccountBalance(), yearsUntilRetirement,
                    incomeSource.getReturnRate(), incomeSource.getReturnFrequency());
        }

        monthlyIncomeAvailable = (totalIncomeAvailable / retirementYears) / 12;

        return (int) Math.round(monthlyIncomeAvailable);
    }

    private double getYearsUntilRetirement(Retiree retiree) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date retireDate = dateFormatter.parse(retiree.getRetirementDate().toString(), new ParsePosition(0));

        double secondsInAYear = 31557600;
        double secondsUntilRetirement = (retireDate.getTime() - System.currentTimeMillis()) / 1000;
        double yearsUntilRetirement = secondsUntilRetirement / secondsInAYear;

        return yearsUntilRetirement;
    }

    private double getFutureValue(int accountBalance, double yearsUntilRetirement, double returnRate,
            int returnFrequency) {

        double interestRate = (returnRate / 100) / returnFrequency;
        double numPeriods = yearsUntilRetirement * returnFrequency;

        return accountBalance * Math.pow((1 + interestRate), numPeriods);
    }

    private int calculateMonthlyExpenses(Retiree retiree, CustomUser authenticatedUser) {
        List<Expense> expenseList = expenseService.getAllExpenses(retiree.getUserId(), authenticatedUser);
        int totalExpenses = 0;

        for (Expense expense : expenseList) {
            totalExpenses += expense.getAmount() * expense.getFrequencyPerYear();
        }

        return totalExpenses / 12;

    }

    private int calculateAdditionalSavings(Retiree retiree, int estimatedMonthlyDisposable, int monthlyDisposableGoal) {
        int monthlyDifference = monthlyDisposableGoal - estimatedMonthlyDisposable;
        return monthlyDifference * retiree.getRetirementYears() * 12;
    }

    private int monthlyToSave(Retiree retiree, int additionalNeeded) {
        double yearsUntilRetirement = getYearsUntilRetirement(retiree);
        int monthsUntilRetirement = (int) yearsUntilRetirement * 12;

        return (int) additionalNeeded / monthsUntilRetirement;
    }
}
