package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codecrafters.quizquest.MainActivity;
import com.codecrafters.quizquest.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class CommonLoginRegistrationActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login_registration);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Set click listener for the Register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle registration logic here
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
                loginUserAccount();

            }
        });
    }

    private void loginUserAccount() {
        String email, password;
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                                "Login successful!!",
                                                Toast.LENGTH_LONG)
                                        .show();


                                // if sign-in is successful
                                // intent to home activity
                                Intent intent
                                        = new Intent(CommonLoginRegistrationActivity.this,
                                        UserDashboardActivity.class);
                                startActivity(intent);
                            }

                            else {

                                // sign-in failed
                                Toast.makeText(getApplicationContext(),
                                                "Wrong Email or password!!",
                                                Toast.LENGTH_LONG)
                                        .show();

                            }
                        });


    }

    // Method to handle the button click and navigate to AdminDashboardActivity
    public void navigateToAdminDashboard(View view) {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
    }
}
