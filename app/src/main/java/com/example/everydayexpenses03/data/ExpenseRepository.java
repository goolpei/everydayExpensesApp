package com.example.everydayexpenses03.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private final LiveData<List<Expense>> allExpenses;
    private final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    // =========================================================================
    // 1. WRITE OPERATIONS (Must be run on a background thread)
    // =========================================================================

    public void insert(Expense expense) {
        databaseWriteExecutor.execute(() -> expenseDao.insert(expense));
    }

    public void delete(Expense expense) {
        databaseWriteExecutor.execute(() -> expenseDao.delete(expense));
    }

    // =========================================================================
    // 2. LIST QUERIES (Room handles background execution for LiveData)
    // =========================================================================

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getRecentExpenses(long startTime) {
        return expenseDao.getRecentExpenses(startTime);
    }

    // =========================================================================
    // 3. STATS & MATH QUERIES (Returning Single Values)
    // =========================================================================

    public LiveData<Double> getTodaysTotal(long startOfDay) {
        return expenseDao.getTodaysTotal(startOfDay);
    }

    public LiveData<Double> getWeeklyTotal(long start, long end) {
        return expenseDao.getTotalSpentInRange(start, end);
    }

    public LiveData<Double> getDailyAverage() {
        return expenseDao.getAverageDailyExpense();
    }
}