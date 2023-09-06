package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.codecrafters.quizquest.R;

public class CommonLoginRegistrationActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner roleSpinner;
    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login_registration);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Set click listener for the Register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle registration logic here
                // You can navigate to the registration activity or perform registration tasks
                // For example:
                Intent registrationIntent = new Intent(CommonLoginRegistrationActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
            }
        });

        // Set click listener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login logic here
                // You can validate user input, authenticate the user, and perform login tasks
                // For example:
//                String email = emailEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//                String selectedRole = roleSpinner.getSelectedItem().toString();

                String selectedRole="User";//Sample As of Now

                // Implement your login logic here

                // After successful login, you can navigate to the appropriate dashboard or user profile page
                // For example:
                if (selectedRole.equals("Admin")) {
                    // Navigate to the Admin Dashboard
                    Intent adminDashboardIntent = new Intent(CommonLoginRegistrationActivity.this, AdminDashboardActivity.class);
                    startActivity(adminDashboardIntent);
                } else if (selectedRole.equals("User")) {
                    // Navigate to the User Dashboard
                    Intent userDashboardIntent = new Intent(CommonLoginRegistrationActivity.this, UserDashboardActivity.class);
                    startActivity(userDashboardIntent);
                }
            }
        });
    }
}
