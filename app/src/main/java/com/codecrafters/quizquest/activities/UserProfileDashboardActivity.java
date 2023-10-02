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
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_dashboard);

        viewModel = new ViewModelProvider(this).get(UserProfileDashboardViewModel.class);

        userId = getIntent().getStringExtra("USER_ID");

        viewModel.loadUserData(userId);
        viewModel.loadQuizHistoryData(userId);

        TextView userNameTextView = findViewById(R.id.userName);
        TextView userQuizCountTextView = findViewById(R.id.userQuizCount);
        ListView quizHistoryListView = findViewById(R.id.quizHistoryList);
        TextView noQuizMessageTextView = findViewById(R.id.noQuizMessage);
        Button startNewQuizButton = findViewById(R.id.startNewQuizButton);

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

                String quizCountText = "Quizzes taken: " + quizCount;
                userQuizCountTextView.setText(quizCountText);
            }
        });

        startNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileDashboardActivity.this, QuizCategoriesActivity.class);
                startActivity(intent);
            }
        });
    }
}