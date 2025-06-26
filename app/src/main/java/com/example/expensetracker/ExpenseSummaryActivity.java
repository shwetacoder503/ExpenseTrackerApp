package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseSummaryActivity extends AppCompatActivity {

    private PieChart pieChart;
    private AppDatabase db;
    private ImageButton logoutbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);

        pieChart = findViewById(R.id.pieChart);
        db = AppDatabase.getInstance(this);
        pieChart.setDrawHoleEnabled(true); // ✅ This is valid
        pieChart.setHoleRadius(40f);       // ✅ Only if recognized after proper import


        SharedPreferences prefs = getSharedPreferences("ExpenseAppPrefs", MODE_PRIVATE);
        String currentUser = prefs.getString("loggedInUser", null);

        if (currentUser != null) {
            List<Expense> expenses = db.expenseDao().getExpensesByUser(currentUser);
            if (expenses != null && !expenses.isEmpty()) {
                showPieChart(expenses);
            } else {
                pieChart.clear();
                pieChart.setNoDataText("No expenses found for this user.");
                Toast.makeText(this, "No data to display", Toast.LENGTH_SHORT).show();
            }
        }
        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            // Clear saved user
            //SharedPreferences prefs = getSharedPreferences("ExpenseAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("loggedInUser");
            editor.apply();

            // Go to LoginActivity
            Intent intent = new Intent(ExpenseSummaryActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear backstack
            startActivity(intent);
        });

    }

    private void showPieChart(List<Expense> expenses) {
        Map<String, Float> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            float amount = expense.amount;
            String category = expense.category;

            if (categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + amount);
            } else {
                categoryTotals.put(category, amount);
            }
        }

        List<PieEntry> pieEntries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Expenses by Category");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);

        Description desc = new Description();
        desc.setText("Category-wise expense breakdown");
        pieChart.setDescription(desc);

        pieChart.invalidate(); // Refresh chart
    }
}
