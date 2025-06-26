package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView register;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        register=findViewById(R.id.register);

        db = AppDatabase.getInstance(this);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                User user = db.userDao().getUserByUsername(username);
                if (user != null && user.password.equals(password)) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getSharedPreferences("ExpenseAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("loggedInUser", username);
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, AddExpenseActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(v->{
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
