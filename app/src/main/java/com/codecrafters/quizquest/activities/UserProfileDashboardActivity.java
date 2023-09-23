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

import java.util.ArrayList;

public class UserProfileDashboardActivity extends AppCompatActivity {

    private UserProfileDashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_dashboard);

        viewModel = new ViewModelProvider(this).get(UserProfileDashboardViewModel.class);

        // Assuming you have a way to get the logged-in user's ID
        String userId = "YOUR_LOGGED_IN_USER_ID"; // You'll need to replace this with actual logic to get the user ID
        viewModel.loadUserData(userId);
        viewModel.loadQuizHistoryData(userId);

        viewModel.getUser().observe(this, user -> {
            TextView userNameTextView = findViewById(R.id.userName);
            userNameTextView.setText(user.getName());

            TextView quizSummaryTextView = findViewById(R.id.quizSummary);
            quizSummaryTextView.setText("Quizzes Taken: " + user.getQuizzesTaken());
        });

        viewModel.getQuizHistory().observe(this, quizHistory -> {
            ListView quizHistoryListView = findViewById(R.id.quizHistoryList);
            QuizHistoryAdapter adapter = new QuizHistoryAdapter(this, new ArrayList<>(quizHistory));
            quizHistoryListView.setAdapter(adapter);
        });

        // Set OnClickListener for the "Start New Quiz" button
        Button startNewQuizButton = findViewById(R.id.startNewQuizButton);
        startNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileDashboardActivity.this, QuizCategoriesActivity.class);
                startActivity(intent);
            }
        });
    }
}
