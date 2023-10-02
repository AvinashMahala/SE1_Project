package com.codecrafters.quizquest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, dobTextView, phonenumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase and FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize TextViews to display user data
        nameTextView = findViewById(R.id.nameEditText);
        emailTextView = findViewById(R.id.emailEditText);
        dobTextView = findViewById(R.id.dobEditText);
        phonenumberTextView = findViewById(R.id.phoneNumberEditText);
        Button dashboardbtn = findViewById(R.id.backTDashboardButton);

        dashboardbtn.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, UserDashboardActivity.class);
            startActivity(intent);
        });
        // Get the currently logged-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Retrieve the user's UID
            String uid = currentUser.getUid();

            // Reference the user's node in the database
            DatabaseReference userRef = firebaseDatabase.getReference("UserINFO").child(uid);

            // Read data from the database and populate the TextViews
            userRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("UserFname").getValue(String.class);
                        String email = dataSnapshot.child("UserEmail").getValue(String.class);
                        String dob = dataSnapshot.child("UserDOB").getValue(String.class);
                        String phonenumber = dataSnapshot.child("UserPhNo").getValue(String.class);

                        // Set the retrieved data to the TextViews
                        nameTextView.setText("Name: " + name);
                        emailTextView.setText("Email: " + email);
                        dobTextView.setText("Date of Birth: " + dob);
                        phonenumberTextView.setText("Phone Number: " + phonenumber);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors here
                }
            });
        }
    }
}