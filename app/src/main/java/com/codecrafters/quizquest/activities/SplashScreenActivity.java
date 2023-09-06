package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.codecrafters.quizquest.R;

public class SplashScreenActivity extends AppCompatActivity {

    // Set the duration of the splash screen in milliseconds (e.g., 5000ms = 5 seconds)
    private static final int SPLASH_DURATION = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Use a Handler to delay the navigation to the RegistrationActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to navigate to the RegistrationActivity
                Intent intent = new Intent(SplashScreenActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish(); // Close the splash screen activity
            }
        }, SPLASH_DURATION);
    }
}
