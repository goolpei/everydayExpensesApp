package com.example.everydayexpenses03.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private final LiveData<List<Expense>> allExpenses;

    // We use this to run database writes (Insert/Delete) in the background
    private final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    // --- METHODS TO GET DATA (Room handles these background tasks automatically) ---

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getWeeklyTotal(long start, long end) {
        return expenseDao.getTotalSpentInRange(start, end);
    }

    public LiveData<Double> getDailyAverage() {
        return expenseDao.getAverageDailyExpense();
    }

    // --- METHODS TO CHANGE DATA (Must be run on a background thread) ---

    public void insert(Expense expense) {
        databaseWriteExecutor.execute(() -> expenseDao.insert(expense));
    }

    public void delete(Expense expense) {
        databaseWriteExecutor.execute(() -> expenseDao.delete(expense));
    }
}