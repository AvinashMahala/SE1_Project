package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.LeaderBoardAdapter;
import com.codecrafters.quizquest.models.QuizTaken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity {
    private ArrayList<QuizTaken> quizTakenList;
    private RecyclerView rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        quizTakenList = new ArrayList<>();
        rc = findViewById(R.id.rcview);
        setLeaderBoardInfo();

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

                setAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
