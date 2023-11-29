package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.LeaderBoardAdapter;
import com.codecrafters.quizquest.models.QuizTaken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderBoardActivity extends AppCompatActivity {
    private ArrayList<QuizTaken> quizTakenList;
    private RecyclerView rc;
    private FirebaseAuth mAuth;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        Button btnCategories = findViewById(R.id.btnCategories);
        Button btnUserDashboard = findViewById(R.id.btnUserDashboard);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToCategories();
            }
        });

        btnUserDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToUserDashboard();
            }
        });
        quizTakenList = new ArrayList<>();
        rc = findViewById(R.id.rcview);
        setLeaderBoardInfo();
        setAdapter();
    }

    private void navigateToUserDashboard() {
        Intent intent = new Intent(this, UserDashboardActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void navigateToCategories() {
        Intent intent = new Intent(this, QuizCategoriesActivity.class);
        startActivity(intent);
    }

    private void setAdapter() {
        LeaderBoardAdapter adapter = new LeaderBoardAdapter(this, quizTakenList);
        rc.setAdapter(adapter);
        rc.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setLeaderBoardInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("QuizTaken");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizTakenList.clear();

                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    QuizTaken quizTaken = quizSnapshot.getValue(QuizTaken.class);
                    quizTakenList.add(quizTaken);
                }
                // Sort the list based on scores
                Collections.sort(quizTakenList, new Comparator<QuizTaken>() {
                    @Override
                    public int compare(QuizTaken quiz1, QuizTaken quiz2) {
                        // Reverse order (high scores first)
                        return Integer.compare(quiz2.getQuizTakenScore(), quiz1.getQuizTakenScore());
                    }
                });

                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
