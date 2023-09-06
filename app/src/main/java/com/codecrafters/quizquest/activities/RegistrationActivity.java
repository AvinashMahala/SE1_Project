package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.codecrafters.quizquest.R;

public class RegistrationActivity extends AppCompatActivity {
    // Implement user registration logic here

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Handle user registration when they sign up
        // Redirect to the appropriate dashboard based on user role
    }
}
