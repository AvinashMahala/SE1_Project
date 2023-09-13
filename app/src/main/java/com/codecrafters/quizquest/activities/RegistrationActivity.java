package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.codecrafters.quizquest.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailTextView, passwordTextView, nameTextView, phonenumberTextView, dobTextView;
    private Button Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Find the "Back to Common Login" button by its ID
        Button backToCommonButton = findViewById(R.id.backToCommonButton);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.emailEditText);
        passwordTextView = findViewById(R.id.passwordEditText);
        String uid = mAuth.getCurrentUser().getUid();
        Btn = findViewById(R.id.registerButton);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });

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

    private void registerNewUser() {
        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // Validations for input email and password
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

        // Create a new user or register a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // If the user is created, navigate to the login activity
                            Intent intent = new Intent(RegistrationActivity.this, CommonLoginRegistrationActivity.class);
                            startActivity(intent);
                        } else {
                            // Registration failed, handle exceptions
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                // Handle case where email is already in use
                                Toast.makeText(getApplicationContext(),
                                                "Email is already in use!",
                                                Toast.LENGTH_LONG)
                                        .show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                // Handle invalid email format
                                Toast.makeText(getApplicationContext(),
                                                "Invalid email or password format!",
                                                Toast.LENGTH_LONG)
                                        .show();
                            } catch (Exception e) {
                                // Handle other exceptions
                                Toast.makeText(getApplicationContext(),
                                                "Registration failed: " + e.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }
                });
    }

}
