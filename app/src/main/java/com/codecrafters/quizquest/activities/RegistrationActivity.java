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

import com.codecrafters.quizquest.MainActivity;
import com.codecrafters.quizquest.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private EditText emailTextView, passwordTextView, nameTextView, phonenumberTextView, dobTextView;
    private Button Btn;
     String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Find the "Back to Common Login" button by its ID
        Button backToCommonButton = findViewById(R.id.backToCommonButton);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        nameTextView = findViewById(R.id.nameEditText);
        phonenumberTextView = findViewById(R.id.phoneNumberEditText);
        dobTextView = findViewById(R.id.dobEditText);
        emailTextView = findViewById(R.id.emailEditText);
        passwordTextView = findViewById(R.id.passwordEditText);
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
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration was successful
                                uid = mAuth.getCurrentUser().getUid();

                                // Create a user data map
                                Map<String, Object> user = new HashMap<>();
                                user.put("Fname", name);
                                user.put("Email", email);
                                user.put("DOB", dob);
                                user.put("Phn", phnno);

                                // Reference the specific user's node using their UID
                                DatabaseReference userRef = firebaseDatabase.getReference("UserINFO").child(uid);

                                // Set the user data in the database
                                userRef.setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // If data is added successfully, show a success message
                                                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                                                    // Navigate to the login activity
                                                    Intent intent = new Intent(RegistrationActivity.this, CommonLoginRegistrationActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    // Handle the case where data couldn't be added to the database
                                                    Toast.makeText(getApplicationContext(), "Failed to add user data to the database", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                // Registration failed, handle exceptions
                                try {
                                    throw task.getException();
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
                        }
                    });
        }
    }


}
