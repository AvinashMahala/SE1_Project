package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codecrafters.quizquest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private EditText emailTextView, passwordTextView, nameTextView, phonenumberTextView, dobTextView;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeUI();
    }

    private void initializeUI() {
        Button backToCommonButton = findViewById(R.id.backTDashboardButton);
        Button registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        nameTextView = findViewById(R.id.nameEditText);
        phonenumberTextView = findViewById(R.id.phoneNumberEditText);
        dobTextView = findViewById(R.id.dobEditText);
        emailTextView = findViewById(R.id.emailEditText);
        passwordTextView = findViewById(R.id.passwordEditText);

        registerButton.setOnClickListener(v -> registerNewUser());
        backToCommonButton.setOnClickListener(view -> navigateTo(CommonLoginRegistrationActivity.class));
    }

    private void registerNewUser() {
        try {
            String email = emailTextView.getText().toString().trim();
            String password = passwordTextView.getText().toString().trim();
            String name = nameTextView.getText().toString().trim();
            String dob = dobTextView.getText().toString().trim();
            String phnno = phonenumberTextView.getText().toString().trim();

            if (areFieldsEmpty(name, phnno, dob, email, password)) {
                showToast("Please fill all required fields.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    handleSuccessfulRegistration(name, email, dob, phnno);
                } else {
                    handleRegistrationExceptions(task.getException());
                }
            });
        } catch (Exception e) {
            showToast("Unexpected error: " + e.getMessage());
        }
    }

    private boolean areFieldsEmpty(String... fields) {
        for (String field : fields) {
            if (TextUtils.isEmpty(field)) return true;
        }
        return false;
    }

    private void handleSuccessfulRegistration(String name, String email, String dob, String phnno) {
        uid = mAuth.getCurrentUser().getUid();
        Date currentTime = Calendar.getInstance().getTime();
        Map<String, Object> user = createUserMap(name, email, dob, phnno, currentTime);

        DatabaseReference userRef = firebaseDatabase.getReference("UserINFO").child(uid);
        userRef.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("Registration successful!");
                navigateTo(CommonLoginRegistrationActivity.class);
            } else {
                showToast("Failed to add user data to the database");
            }
        });
    }

    private Map<String, Object> createUserMap(String name, String email, String dob, String phnno, Date currentTime) {
        Map<String, Object> user = new HashMap<>();
        user.put("UserFname", name);
        user.put("UserEmail", email);
        user.put("UserDOB", dob);
        user.put("UserPhNo", phnno);
        user.put("UserRole", "User");
        user.put("Created on", currentTime.toString());
        user.put("Modified on", "Null");
        return user;
    }

    private void handleRegistrationExceptions(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            showToast("Email is already in use!");
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            showToast("Invalid email or password format!");
        } else {
            showToast("Registration failed: " + exception.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        finish();
    }
}
