package com.codecrafters.quizquest.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.codecrafters.quizquest.R;

public class QuizSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_set);

        // Find the TextView
        TextView adminQuizSetWelcome = findViewById(R.id.adminQuizSetWelcome);

        // Get the category name from the Intent extras
        String categoryName = getIntent().getStringExtra("QUIZ_CATEGORY_NM");

        // Check if categoryName is not null
        if (categoryName != null) {
            // Set the text of the TextView using the category name
            adminQuizSetWelcome.setText("Welcome to " + categoryName + " Quiz Sets!");
        } else {
            // Handle the case where categoryName is null or not provided
            adminQuizSetWelcome.setText("Welcome to Quiz Set Area!");
        }

    }

}