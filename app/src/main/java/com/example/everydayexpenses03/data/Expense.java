package com.example.everydayexpenses03.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;  // From your Spinner/Dropdown
    private double amount;    // From your Amount EditText
    private String note;      // From your Additional Notes EditText
    private long timestamp;   // Generated automatically when saving

    // Constructor
    public Expense(String category, double amount, String note, long timestamp) {
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}