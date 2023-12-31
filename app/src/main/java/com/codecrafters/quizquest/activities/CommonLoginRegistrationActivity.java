package com.codecrafters.quizquest.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.codecrafters.quizquest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class CommonLoginRegistrationActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_login_registration);

        initializeUIComponents();
    }

    private void initializeUIComponents() {
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(v -> resetPassword());
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(v -> navigateToRegistration());
        loginButton.setOnClickListener(v -> loginUserAccount());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showToast("Enter your registered email");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Check your email to reset your password");
                    } else {
                        showToast("Failed to send reset email");
                    }
                });
    }

    private void navigateToRegistration() {
        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registrationIntent);
    }

    private void loginUserAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (!validateInputs(email, password)) return;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        navigateToDashboard();
                    } else {
                        showToast("Wrong Email or password!");
                    }
                });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            showToast("Please enter email!");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Please enter password!");
            return false;
        }

        return true;
    }

    private void navigateToDashboard() {
        showToast("Login successful!");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("UserINFO").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Log a message to indicate that onDataChange is triggered
                    Log.d("Firebase", "onDataChange triggered");

                    String userRole = dataSnapshot.child("UserRole").getValue(String.class);
                    Intent intent;

                    if ("admin".equalsIgnoreCase(userRole)) {
                        intent = new Intent(CommonLoginRegistrationActivity.this, AdminDashboardActivity.class);
                    } else {
                        intent = new Intent(CommonLoginRegistrationActivity.this, UserDashboardActivity.class);
                    }
                    intent.putExtra("USER_ID", uid); // Replace userId with the actual user ID
                    startActivity(intent);

                    startActivity(intent);
                } else {
                    // Log a message to indicate that no data exists
                    Log.d("Firebase", "No data found");

                    // Handle the case where the user data doesn't exist
                    showToast("User data not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log the error message
                Log.e("Firebase", "Database error: " + error.getMessage());

                // Handle database errors here
                showToast("Database error: " + error.getMessage());
            }
        });

    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void navigateToAdminDashboard(View view) {
        Intent intent = new Intent(this, AdminDashboardActivity.class);
        startActivity(intent);
    }
}
