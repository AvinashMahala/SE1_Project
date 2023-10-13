package com.codecrafters.quizquest.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.QuizHistoryAdapter;
import com.codecrafters.quizquest.viewmodels.UserProfileDashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserDashboardActivity extends AppCompatActivity {

    private UserProfileDashboardViewModel viewModel;
    private String userId;
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        try {
            viewModel = new ViewModelProvider(this).get(UserProfileDashboardViewModel.class);
            mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

            // Check if userId is not null
            if (getIntent().hasExtra("USER_ID")) {
                userId = getIntent().getStringExtra("USER_ID");

                // Load user data and quiz history based on the received user ID
                viewModel.loadUserData(userId);
                viewModel.loadQuizHistoryData(userId);
            } else {
                // Handle the case where userId is null
                handleException("User ID is null.");
            }

            // Load user data and quiz history based on the received user ID
            viewModel.loadUserData(userId);
            viewModel.loadQuizHistoryData(userId);

            // Find and initialize the TextViews and ListView
            TextView userNameTextView = findViewById(R.id.userName);
            TextView userQuizCountTextView = findViewById(R.id.userQuizCount);
            ListView quizHistoryListView = findViewById(R.id.quizHistoryList);
            TextView noQuizMessageTextView = findViewById(R.id.noQuizMessage);
            Button startNewQuizButton = findViewById(R.id.startNewQuizButton);
            Button userDashboardlogoutButton = findViewById(R.id.userDashboardlogoutButton); // Logout button
            Button userProfileButton = findViewById(R.id.userProfileButton); // User profile button

            viewModel.getUser().observe(this, user -> {
                try {
                    userNameTextView.setText("Welcome " + user.getName());
                    userQuizCountTextView.setText("Quizzes taken: " + user.getQuizzesTaken());
                } catch (Exception e) {
                    handleException("Error loading user data: " + e.getMessage());
                }
            });

            viewModel.getQuizHistory().observe(this, quizHistory -> {
                try {
                    int quizCount = quizHistory.size();

                    if (quizCount == 0) {
                        noQuizMessageTextView.setText("No quizzes taken yet.");
                        quizHistoryListView.setVisibility(View.GONE);
                    } else {
                        noQuizMessageTextView.setVisibility(View.GONE);
                        quizHistoryListView.setVisibility(View.VISIBLE);

                        // Create an adapter to display the quiz history in the ListView
                        QuizHistoryAdapter adapter = new QuizHistoryAdapter(this, new ArrayList<>(quizHistory));
                        quizHistoryListView.setAdapter(adapter);

                        // Display the quiz count
                        String quizCountText = "Quizzes taken: " + quizCount;
                        userQuizCountTextView.setText(quizCountText);
                    }
                } catch (Exception e) {
                    handleException("Error loading quiz history: " + e.getMessage());
                }
            });

            // Set OnClickListener for the "Start New Quiz" button
            startNewQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(UserDashboardActivity.this, QuizCategoriesActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        handleException("Error starting a new quiz: " + e.getMessage());
                    }
                }
            });

            // Set OnClickListener for the "Logout" button
            userDashboardlogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mAuth.signOut(); // Sign out from Firebase Authentication
                        Intent intent = new Intent(UserDashboardActivity.this, CommonLoginRegistrationActivity.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                    } catch (Exception e) {
                        handleException("Error logging out: " + e.getMessage());
                    }
                }
            });

            // Set OnClickListener for the "User Profile" button
            userProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(UserDashboardActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        handleException("Error opening user profile: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            handleException("Error in onCreate: " + e.getMessage());
        }
    }

    private void handleException(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
