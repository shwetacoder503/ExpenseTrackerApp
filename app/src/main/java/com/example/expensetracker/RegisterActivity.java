package com.example.expensetracker;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername1, editTextPassword1, confirmPassword;
    private Button buttonLogin1;
    private TextView register1;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ... your existing code ...
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextUsername1 = findViewById(R.id.editTextUsername1);
        editTextPassword1 = findViewById(R.id.editTextPassword1);
        confirmPassword = findViewById(R.id.confirm);
        buttonLogin1 = findViewById(R.id.buttonLogin1);
        register1=findViewById(R.id.register1);
        db = AppDatabase.getInstance(this);

        buttonLogin1.setOnClickListener(v -> {
            String username = editTextUsername1.getText().toString().trim();
            String password = editTextPassword1.getText().toString().trim();
            String confirm = confirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                User existingUser = db.userDao().getUserByUsername(username);
                if (existingUser != null) {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ CREATE THE USER OBJECT FIRST
                    User user = new User();
                    user.username = username;
                    user.password = password;


                    //  INSERT IT INTO ROOM DB
                    db.userDao().insertUser(user);

                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();

                    //  Optional: Log user list to Logcat
                    List<User> users = db.userDao().getAllUsers();
                    for (User u : users) {
                        Log.d("UserList", "ID: " + u.id + ", Username: " + u.username + ", Password: " + u.password);
                    }

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        register1.setOnClickListener(v->{
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}