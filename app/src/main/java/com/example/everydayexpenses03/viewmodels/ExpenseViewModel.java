package com.example.everydayexpenses03.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.everydayexpenses03.data.Expense;
import com.example.everydayexpenses03.data.ExpenseRepository;
import com.example.everydayexpenses03.utils.DateUtils;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final MutableLiveData<long[]> historyDateFilter = new MutableLiveData<>();
    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;

    public final LiveData<List<Expense>> filteredHistoryExpenses;
    public final LiveData<Double> filteredHistoryTotal;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();

        filteredHistoryExpenses = Transformations.switchMap(historyDateFilter, range ->
                repository.getExpensesByDate(range[0], range[1])
        );

        filteredHistoryTotal = Transformations.switchMap(historyDateFilter, range ->
                repository.getWeeklyTotal(range[0], range[1]) // Reusing your range sum query
        );
    }

    // =========================================================================
    // 1. WRITE OPERATIONS (Events from UI)
    // =========================================================================

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }
    public void setHistoryDate(long start, long end) {
        historyDateFilter.setValue(new long[]{start, end});
    }

    // =========================================================================
    // 2. LIST QUERIES (Data for RecyclerViews)
    // =========================================================================

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getRecentExpenses() {
        long twoDaysAgo = DateUtils.getTwoDaysAgoStart();
        return repository.getRecentExpenses(twoDaysAgo);
    }
    public LiveData<List<Expense>> getExpensesByDate(int year, int month, int day) {
        long start = DateUtils.getStartOfSpecificDay(year, month, day);
        long end = DateUtils.getEndOfSpecificDay(year, month, day);
        return repository.getExpensesByDate(start, end);
    }

    // =========================================================================
    // 3. STATS & MATH QUERIES (Data for Summary Cards)
    // =========================================================================

    public LiveData<Double> getTodaysTotal() {
        return repository.getTodaysTotal(DateUtils.getStartOfDay());
    }

    public LiveData<Double> getWeeklyTotal() {
        long start = DateUtils.getStartOfWeek();
        long end = System.currentTimeMillis() + 86400000; // Today + 1 day padding
        return repository.getWeeklyTotal(start, end);
    }

    public LiveData<Double> getMonthlyTotal() {
        long start = DateUtils.getStartOfMonth();
        long now = System.currentTimeMillis();
        return repository.getWeeklyTotal(start, now); // Reuses the same "Range" query from Repo
    }

    public LiveData<Double> getDailyAverage() {
        return repository.getDailyAverage();
    }
    public LiveData<Double> getTotalForSpecificDay(int year, int month, int day) {
        long start = DateUtils.getStartOfSpecificDay(year, month, day);
        long end = DateUtils.getEndOfSpecificDay(year, month, day);
        return repository.getWeeklyTotal(start, end); // Reusing the range sum query!
    }
}