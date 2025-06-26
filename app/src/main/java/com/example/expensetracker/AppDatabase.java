package com.example.expensetracker;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ExpenseDao expenseDao(); // ✅ Added for expenses

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "user_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()  // For simplicity (not for production)
                    .build();
        }
        return INSTANCE;
    }
}
