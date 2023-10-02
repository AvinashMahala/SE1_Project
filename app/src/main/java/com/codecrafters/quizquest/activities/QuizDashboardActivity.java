package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codecrafters.quizquest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizDashboardActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Spinner quizSetSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_dashboard2);


        ImageView quizCategoryImage = findViewById(R.id.quizCategoryImage);
        TextView quizCategoryName = findViewById(R.id.quizCategoryName);
        TextView quizCategoryDescription = findViewById(R.id.quizCategoryDescription);
        Button startQuizButton = findViewById(R.id.startQuizButton);

        quizSetSpinner = findViewById(R.id.quizSetSpinner);
        String[] quizSets = {"Quiz Set 1", "Quiz Set 2", "Quiz Set 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quizSets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        quizSetSpinner.setAdapter(adapter);


        // Get the quiz name passed from QuizCategoriesActivity
        String quizName = getIntent().getStringExtra("quizName");

        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("QuizCategory");

        // Query the database to fetch the data for the selected quiz category
        databaseReference.orderByChild("quizCatName").equalTo(quizName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Fetch data from Firebase
                    String quizCatImg = snapshot.child("quizCatImg").getValue(String.class);
                    String quizCatDescription = snapshot.child("quizCatDescription").getValue(String.class);

                    // Set the fetched data to the UI elements
                    quizCategoryName.setText(quizName);
                    quizCategoryDescription.setText(quizCatDescription);

                    // Load the image (you might want to use a library like Picasso or Glide)
                    // Example using Glide:
                    Glide.with(QuizDashboardActivity.this).load(quizCatImg).into(quizCategoryImage);

                    // Set an onClickListener for the startQuizButton if needed
                    startQuizButton.setOnClickListener(v -> {
                        // Handle button click, e.g., start the quiz
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
