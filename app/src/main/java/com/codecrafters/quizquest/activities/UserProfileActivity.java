package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private TextView nameTextView, EmailTextView, DobTextView, PhnTextView;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseDatabase = FirebaseDatabase.getInstance();
        nameTextView = findViewById(R.id.nameEditText);
        EmailTextView = findViewById(R.id.emailEditText);
        DobTextView = findViewById(R.id.dobEditText);
        PhnTextView = findViewById(R.id.phoneNumberEditText);
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

    }
}