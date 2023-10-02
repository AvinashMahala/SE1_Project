package com.codecrafters.quizquest.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
public class UserDashboardActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Retrieve user statistics data (replace with actual data)
        int userScore = 85;
        String userPerformance = "Excellent";

        // Find the TextViews by their IDs
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        TextView performanceTextView = findViewById(R.id.performanceTextView);

        // Set the user statistics data to the TextViews
        scoreTextView.setText("Score: " + userScore);
        performanceTextView.setText("Performance: " + userPerformance);

        // Find the "Start Quiz" button by its ID
        Button startQuizButton = findViewById(R.id.startQuizButton);

        Button btn = findViewById(R.id.ProfiletButton);

        btn.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

// Set a click listener for the button
        startQuizButton.setOnClickListener(view -> {
            // Create an intent to navigate to the QuizCategoryActivity
            Intent intent = new Intent(UserDashboardActivity.this, QuizCategoriesActivity.class);
            startActivity(intent);
        });


        // Find the "Logout" button by its ID
        Button logoutButton = findViewById(R.id.logoutButton);

        // Set a click listener for the button
        logoutButton.setOnClickListener(view -> {
            // Create an intent to navigate back to the CommonLoginRegistrationActivity
            Intent intent = new Intent(UserDashboardActivity.this, CommonLoginRegistrationActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity
        });
    }
}
