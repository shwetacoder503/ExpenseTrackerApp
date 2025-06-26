package com.example.expensetracker;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String category;      // Food, Travel, etc.
    public String description;   // Optional note
    public float amount;        // Money spent
    public String date;
    @NonNull
    public String username;      // Logged-in user// Date of expense (e.g. 2024-06-24)
}
