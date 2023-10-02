package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.QuizHistoryAdapter;
import com.codecrafters.quizquest.viewmodels.UserProfileDashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserProfileDashboardActivity extends AppCompatActivity {

    private UserProfileDashboardViewModel viewModel;
    private String userId;
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_dashboard);

        viewModel = new ViewModelProvider(this).get(UserProfileDashboardViewModel.class);
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        userId = getIntent().getStringExtra("USER_ID");

        // Load user data and quiz history based on the received user ID
        viewModel.loadUserData(userId);
        viewModel.loadQuizHistoryData(userId);

        // Find and initialize the TextViews and ListView
        TextView userNameTextView = findViewById(R.id.userName);
        TextView userQuizCountTextView = findViewById(R.id.userQuizCount);
        ListView quizHistoryListView = findViewById(R.id.quizHistoryList);
        TextView noQuizMessageTextView = findViewById(R.id.noQuizMessage);
        Button startNewQuizButton = findViewById(R.id.startNewQuizButton);
        Button logoutButton = findViewById(R.id.logoutButton); // Logout button
        Button userProfileButton = findViewById(R.id.userProfileButton); // User profile button

        viewModel.getUser().observe(this, user -> {
            userNameTextView.setText("Welcome " + user.getName());
            userQuizCountTextView.setText("Quizzes taken: " + user.getQuizzesTaken());
        });

        viewModel.getQuizHistory().observe(this, quizHistory -> {
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
        });

        // Set OnClickListener for the "Start New Quiz" button
        startNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileDashboardActivity.this, QuizCategoriesActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the "Logout" button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut(); // Sign out from Firebase Authentication
                Intent intent = new Intent(UserProfileDashboardActivity.this, CommonLoginRegistrationActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });

        // Set OnClickListener for the "User Profile" button
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileDashboardActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}