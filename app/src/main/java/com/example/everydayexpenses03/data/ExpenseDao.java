package com.example.everydayexpenses03.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ExpenseDao {

    // =========================================================================
    // 1. WRITE OPERATIONS (The "Inputs")
    // =========================================================================

    @Insert
    void insert(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expense_table")
    void deleteAllExpenses();

    // =========================================================================
    // 2. LIST QUERIES (The "Logs")
    // =========================================================================

    @Query("SELECT * FROM expense_table ORDER BY timestamp DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expense_table WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    LiveData<List<Expense>> getRecentExpenses(long startTime);
    @Query("SELECT * FROM expense_table WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    LiveData<List<Expense>> getExpensesByDate(long startTime, long endTime);

    // =========================================================================
    // 3. STATS & MATH QUERIES (The "Aggregates")
    // =========================================================================

    @Query("SELECT SUM(amount) FROM expense_table WHERE timestamp >= :startOfDay")
    LiveData<Double> getTodaysTotal(long startOfDay);

    @Query("SELECT SUM(amount) FROM expense_table WHERE timestamp BETWEEN :startTime AND :endTime")
    LiveData<Double> getTotalSpentInRange(long startTime, long endTime);

    @Query("SELECT SUM(amount) / COUNT(DISTINCT date(timestamp/1000, 'unixepoch')) FROM expense_table")
    LiveData<Double> getAverageDailyExpense();

    @Query("SELECT SUM(amount) FROM expense_table WHERE timestamp >= :startTime")
    LiveData<Double> getTotalFromDate(long startTime);
}