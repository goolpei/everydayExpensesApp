package com.example.everydayexpenses03.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// We list all entities (tables) here. Version 1 is our starting point.
@Database(entities = {Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // This links the DAO to the Database
    public abstract ExpenseDao expenseDao();

    private static volatile AppDatabase INSTANCE;

    // This method gets the existing database or creates a new one if it doesn't exist
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "expense_database")
                            .fallbackToDestructiveMigration() // Helps if you change the schema later
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}