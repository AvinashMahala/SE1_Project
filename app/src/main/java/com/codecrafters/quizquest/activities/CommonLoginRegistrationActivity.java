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
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        // Set click listener for the Register button
        registerButton.setOnClickListener(v -> {
            // Handle registration logic here
            Intent registrationIntent = new Intent(CommonLoginRegistrationActivity.this, RegistrationActivity.class);
            startActivity(registrationIntent);
        });

        // Set click listener for the Login button
        loginButton.setOnClickListener(v -> {
            // Handle login logic here
            loginUserAccount();

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

        // Sign in existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                                "Login successful!!",
                                                Toast.LENGTH_LONG)
                                        .show();


                                // if sign-in is successful
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                // Get the currently logged-in user
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                if (currentUser != null) {
                                    // Retrieve the user's UID
                                    String uid = currentUser.getUid();

                                    // Reference the user's node in the database
                                    userRef = firebaseDatabase.getReference("UserINFO").child(uid);

                                    // Read data from the database and populate the TextViews
                                    userRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String userprofile = dataSnapshot.child("UserRole").getValue(String.class);
                                                Intent intent;
                                                if(Objects.equals(userprofile, "Admin")) {
                                                    intent = new Intent(CommonLoginRegistrationActivity.this,
                                                            AdminDashboardActivity.class);
                                                }
                                                else    {
                                                    // intent to home activity
                                                    intent = new Intent(CommonLoginRegistrationActivity.this,
                                                            UserDashboardActivity.class);
                                                }
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
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
