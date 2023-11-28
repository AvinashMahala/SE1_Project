package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codecrafters.quizquest.R;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultsPage extends AppCompatActivity {

    private String quizName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results_page);

        // Retrieve data from the intent
        int score = getIntent().getIntExtra("score", 0);
         quizName = getIntent().getStringExtra("quizCat");

        // Display the data in TextViews
        TextView scoreTextView = findViewById(R.id.textViewScore);
        TextView quizNameTextView = findViewById(R.id.textViewQuizName);

        scoreTextView.setText("Your Score: " + score);

        // Get references to the "Finish" and "Retry" buttons
        Button finishButton = findViewById(R.id.buttonFinish);
        Button retryButton = findViewById(R.id.retryFinish);

        // Set OnClickListener for the "Finish" button
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "Finish" button click
                finishQuiz();
            }
        });

        // Set OnClickListener for the "Retry" button
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the "Retry" button click
                retryQuiz();
            }
        });
    }

    // Method to handle "Finish" button click
    private void finishQuiz() {
        // Add code to perform actions when "Finish" button is clicked

        // Example: Navigate to another activity (replace YourNextActivity.class with the actual class you want to navigate to)
        Intent intent = new Intent(QuizResultsPage.this, LeaderBoardActivity.class);
        startActivity(intent);

        // Finish the current activity (optional, depending on your navigation requirements)
        finish();
    }

    private void retryQuiz() {
        // Add code to perform actions when "Retry" button is clicked

        // Example: Restart the current activity (replace QuizResultsActivity.class with the actual class of the quiz activity)
        Intent intent = new Intent(QuizResultsPage.this, QuizDashboardActivity.class);
        intent.putExtra("catKey", quizName);
        startActivity(intent);

        // Finish the current activity (optional, depending on your navigation requirements)
        finish();
    }
}
