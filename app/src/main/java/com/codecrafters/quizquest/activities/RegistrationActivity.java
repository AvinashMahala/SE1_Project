package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.codecrafters.quizquest.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Find the "Back to Common Login" button by its ID
        Button backToCommonButton = findViewById(R.id.backToCommonButton);

        // Set a click listener for the button
        backToCommonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to navigate to the CommonLoginRegistrationActivity
                Intent intent = new Intent(RegistrationActivity.this, CommonLoginRegistrationActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }
}
