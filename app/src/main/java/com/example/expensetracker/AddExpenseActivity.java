package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText editAmount, editDescription, editDate;
    private Spinner spinnerCategory;
    private Button buttonSubmit, buttonPrevious, viewSummaryButton;

    private AppDatabase db;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Bind UI elements
        editAmount = findViewById(R.id.editAmount);
        editDescription = findViewById(R.id.editDescription);
        editDate = findViewById(R.id.editDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        viewSummaryButton = findViewById(R.id.buttonViewSummary);

        // Get logged-in user from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("ExpenseAppPrefs", MODE_PRIVATE);
        loggedInUsername = prefs.getString("loggedInUser", "unknown");

        // Database instance
        db = AppDatabase.getInstance(this);

        // Spinner setup
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Food", "Transport", "Shopping", "Bills", "Other"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Date Picker setup
        editDate.setOnClickListener(v -> showDatePicker());

        // Submit button logic
        buttonSubmit.setOnClickListener(v -> {
            String amountStr = editAmount.getText().toString();
            String description = editDescription.getText().toString();
            String date = editDate.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (amountStr.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in amount and date", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            Expense expense = new Expense();
            expense.username = loggedInUsername;
            expense.amount = (float) amount;
            expense.description = description;
            expense.date = date;
            expense.category = category;

            db.expenseDao().insertExpense(expense);
            Toast.makeText(this, "Expense saved successfully!", Toast.LENGTH_SHORT).show();
        });

        viewSummaryButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddExpenseActivity.this, ExpenseSummaryActivity.class);
            startActivity(intent);
        });



        // Debug log to show saved expenses
        List<Expense> allExpenses = db.expenseDao().getExpensesByUser(loggedInUsername);
        for (Expense exp : allExpenses) {
            Log.d("EXPENSE_LOG", "ID: " + exp.id +
                    ", Category: " + exp.category +
                    ", Amount: " + exp.amount +
                    ", Date: " + exp.date +
                    ", Desc: " + exp.description);
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            editDate.setText(selectedDate);
        }, year, month, day).show();
    }
}
