package com.example.everydayexpenses03.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ExpenseDao {

    // 1. Input Expense: Standard insert method
    @Insert
    void insert(Expense expense);



    // 2. Daily Log: Get all expenses sorted by newest first
    @Query("SELECT * FROM expense_table ORDER BY timestamp DESC")
    LiveData<List<Expense>> getAllExpenses();


    // Get recent expenses (two days ago)
    @Query("SELECT * FROM expense_table WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    LiveData<List<Expense>> getRecentExpenses(long startTime);


    // 3. Weekly/Monthly Summary: Sum totals between two time periods
    // We will pass the start and end times from our DateUtils
    @Query("SELECT SUM(amount) FROM expense_table WHERE timestamp BETWEEN :startTime AND :endTime")
    LiveData<Double> getTotalSpentInRange(long startTime, long endTime);






    // 4. Average Expenses per Day:
    // This divides the total sum by the count of unique days found in the table
    @Query("SELECT SUM(amount) / COUNT(DISTINCT date(timestamp/1000, 'unixepoch')) FROM expense_table")
    LiveData<Double> getAverageDailyExpense();

    // 5. Delete a specific expense
    // Room uses the Primary Key (id) to find exactly which row to remove
    @Delete
    void delete(Expense expense);

    // 6. Clear all data (Optional, but very useful for testing)
    @Query("DELETE FROM expense_table")
    void deleteAllExpenses();





    @Query("SELECT SUM(amount) FROM expense_table WHERE timestamp >= :startOfDay")
    LiveData<Double> getTodaysTotal(long startOfDay);
}