package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codecrafters.quizquest.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    private EditText emailTextView, passwordTextView, nameTextView, phonenumberTextView, dobTextView;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button backToCommonButton = findViewById(R.id.backTDashboardButton);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        nameTextView = findViewById(R.id.nameEditText);
        phonenumberTextView = findViewById(R.id.phoneNumberEditText);
        dobTextView = findViewById(R.id.dobEditText);
        emailTextView = findViewById(R.id.emailEditText);
        passwordTextView = findViewById(R.id.passwordEditText);
        Button btn = findViewById(R.id.registerButton);

        btn.setOnClickListener(v -> registerNewUser());

        // Set a click listener for the button
        backToCommonButton.setOnClickListener(view -> {
            // Create an intent to navigate to the CommonLoginRegistrationActivity
            Intent intent = new Intent(RegistrationActivity.this, CommonLoginRegistrationActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity
        });
    }

    private void registerNewUser() {
        // Take the value of edit texts
        String email, password, name, phnno, dob;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        name = nameTextView.getText().toString();
        dob = dobTextView.getText().toString();
        phnno = phonenumberTextView.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phnno) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // If any of the text fields are empty, show an error message.
            Toast.makeText(RegistrationActivity.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
        } else {
            // Create a new user in Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration was successful
                            uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            Date currentTime = Calendar.getInstance().getTime();

                            // Create a user data map
                            Map<String, Object> user = new HashMap<>();
                            user.put("UserFname", name);
                            user.put("UserEmail", email);
                            user.put("UserDOB", dob);
                            user.put("UserPhNo", phnno);
                            user.put("UserRole", "User");
                            user.put("Created on", currentTime.toString());
                            user.put("Modified on", "Null");

                            // Reference the specific user's node using their UID
                            DatabaseReference userRef = firebaseDatabase.getReference("UserINFO").child(uid);

                            // Set the user data in the database
                            userRef.setValue(user)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // If data is added successfully, show a success message
                                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                                            // Navigate to the login activity
                                            Intent intent = new Intent(RegistrationActivity.this, CommonLoginRegistrationActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // Handle the case where data couldn't be added to the database
                                            Toast.makeText(getApplicationContext(), "Failed to add user data to the database", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            // Registration failed, handle exceptions
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthUserCollisionException e) {
                                // Handle case where email is already in use
                                Toast.makeText(getApplicationContext(), "Email is already in use!", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                // Handle invalid email format
                                Toast.makeText(getApplicationContext(), "Invalid email or password format!", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // Handle other exceptions
                                Toast.makeText(getApplicationContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


}
