package com.codecrafters.quizquest.activities;

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

            // Retrieve userId either from Intent or FirebaseAuth
            if (getIntent().hasExtra("USER_ID")) {
                userId = getIntent().getStringExtra("USER_ID");
            } else if (mAuth.getCurrentUser() != null) {
                userId = mAuth.getCurrentUser().getUid();
            } else {
                handleException("User ID is null and user is not logged in.");
                return; // Exit the onCreate method
            }

            // Load user data and quiz history based on the retrieved user ID
            viewModel.loadUserData(userId);
            viewModel.loadQuizHistoryData(userId);

            // Initialize UI components
            setupUI();
        } catch (Exception e) {
            handleException("Error in onCreate: " + e.getMessage());
        }
    }

    private void setupUI() {
        TextView userNameTextView = findViewById(R.id.userName);
        TextView userQuizCountTextView = findViewById(R.id.userQuizCount);
        ListView quizHistoryListView = findViewById(R.id.quizHistoryList);
        TextView noQuizMessageTextView = findViewById(R.id.noQuizMessage);
        Button startNewQuizButton = findViewById(R.id.startNewQuizButton);
        Button userDashboardlogoutButton = findViewById(R.id.userDashboardlogoutButton); // Logout button
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
                QuizHistoryAdapter adapter = new QuizHistoryAdapter(this, new ArrayList<>(quizHistory));
                quizHistoryListView.setAdapter(adapter);
                userQuizCountTextView.setText("Quizzes taken: " + quizCount);
            }
        });

        startNewQuizButton.setOnClickListener(v -> startNewQuiz());
        userDashboardlogoutButton.setOnClickListener(v -> logout());
        userProfileButton.setOnClickListener(v -> openUserProfile());
    }

    private void startNewQuiz() {
        try {
            Intent intent = new Intent(UserDashboardActivity.this, QuizCategoriesActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            handleException("Error starting a new quiz: " + e.getMessage());
        }
    }

    private void logout() {
        try {
            mAuth.signOut();
            Intent intent = new Intent(UserDashboardActivity.this, CommonLoginRegistrationActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            handleException("Error logging out: " + e.getMessage());
        }
    }

    private void openUserProfile() {
        try {
            Intent intent = new Intent(UserDashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            handleException("Error opening user profile: " + e.getMessage());
        }
    }

    private void handleException(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
