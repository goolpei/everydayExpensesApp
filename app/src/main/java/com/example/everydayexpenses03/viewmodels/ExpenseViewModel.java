package com.example.everydayexpenses03.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.everydayexpenses03.data.Expense;
import com.example.everydayexpenses03.data.ExpenseRepository;
import com.example.everydayexpenses03.utils.DateUtils;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() { return allExpenses; }

    public LiveData<List<Expense>> getRecentExpenses(long startTime) {
        return repository.getRecentExpenses(startTime);
    }


    public void insert(Expense expense) { repository.insert(expense); }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public LiveData<Double> getTodaysTotal() {
        // We get the start of today's timestamp here
        return repository.getTodaysTotal(DateUtils.getStartOfDay());
    }

    // You can add more methods here to expose Repository functions to your Fragments
}