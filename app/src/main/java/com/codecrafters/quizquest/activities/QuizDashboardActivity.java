package com.codecrafters.quizquest.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizDashboardActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Spinner quizSetSpinner;

    Map<String, String> quizSetNameToIdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_dashboard2);
        ImageView quizCategoryImage = findViewById(R.id.quizCategoryImage);
        TextView quizCategoryName = findViewById(R.id.quizCategoryName);
        TextView quizCategoryDescription = findViewById(R.id.quizCategoryDescription);
        Button startQuizButton = findViewById(R.id.startQuizButton);


        quizSetSpinner = findViewById(R.id.quizSetSpinner);
        final String[][] quizSetsArray = new String[1][1];

        String quizName = getIntent().getStringExtra("catKey");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("QuizSet");

        databaseReference.orderByChild("quizCatID").equalTo(quizName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> quizSets = new ArrayList<>();
                for (DataSnapshot quizSetSnapshot : dataSnapshot.getChildren()) {
                    String quizSetName = quizSetSnapshot.child("quizSetName").getValue(String.class);
                    String quizSetID = quizSetSnapshot.child("quizSetId").getValue(String.class);

                    // Populate the map
                    quizSetNameToIdMap.put(quizSetName, quizSetID);

                    quizSets.add(quizSetName);
                }
                quizSetsArray[0] = quizSets.toArray(new String[0]);

                // Create the ArrayAdapter and set it to the Spinner within onDataChange
                ArrayAdapter<String> adapter = new ArrayAdapter<>(QuizDashboardActivity.this, android.R.layout.simple_spinner_item, quizSets);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                quizSetSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });


        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("QuizCategories");

        // Query the database to fetch the data for the selected quiz category
        databaseReference.orderByChild("quizCatID").equalTo(quizName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Fetch data from Firebase
                    String quizNme = snapshot.child("quizCatNm").getValue(String.class);
                    // String quizCatImg = snapshot.child("quizCatImg").getValue(String.class);
                    String quizCatDescription = snapshot.child("quizCatDesc").getValue(String.class);

                    // Set the fetched data to the UI elements
                    quizCategoryName.setText(quizNme);
                    quizCategoryDescription.setText(quizCatDescription);

                    //Glide.with(QuizDashboardActivity.this).load(quizCatImg).into(quizCategoryImage);
                    String quizCatImg = "https://firebasestorage.googleapis.com/v0/b/codecrafters-quizquest.appspot.com/o/astronomy.png?alt=media&token=2601d765-635c-49d9-80d1-214eff9a8390&_gl=1*1kmocji*_ga*MTc4OTIzNzQ5OS4xNjg0Njc9*ga_CW55HF8NVT*MTY5NjIwMDY1OC4xNi4xLjE2OTYyMDA2NjUuNTMuMC4w";
                    Glide.with(QuizDashboardActivity.this).load(quizCatImg).into(quizCategoryImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });


        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the selected quiz set from the spinner
                String selectedQuizSet = quizSetSpinner.getSelectedItem().toString();
                // Use the map to get the corresponding quiz set ID
                String selectedQuizSetID = quizSetNameToIdMap.get(selectedQuizSet);
                // Create an intent to start the QuizQuestionActivity
                Intent intent = new Intent(QuizDashboardActivity.this, QuizQuestionActivity.class);
                intent.putExtra("quizSet", selectedQuizSetID);
                intent.putExtra("quizCat", quizName);

                startActivity(intent);
            }
        });


    }
}
