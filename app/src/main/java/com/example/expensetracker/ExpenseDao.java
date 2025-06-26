package com.example.expensetracker;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insertExpense(Expense expense);


    @Query("SELECT * FROM expenses WHERE username = :username")
    List<Expense> getExpensesByUser(String username);

}


