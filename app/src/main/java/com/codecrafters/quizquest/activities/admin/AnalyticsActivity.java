package com.codecrafters.quizquest.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.AnalyticsAdapter;
import com.codecrafters.quizquest.models.UserPerformance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsActivity extends AppCompatActivity {

    private Spinner spinnerFilterType, spinnerSpecificFilter;
    private RecyclerView recyclerViewAnalytics;
    private AnalyticsAdapter adapter;
    private Map<String, List<Integer>> userScores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        spinnerFilterType = findViewById(R.id.spinnerFilterType);
        spinnerSpecificFilter = findViewById(R.id.spinnerSpecificFilter);
        recyclerViewAnalytics = findViewById(R.id.recyclerViewAnalytics);
        recyclerViewAnalytics.setLayoutManager(new LinearLayoutManager(this));

        setupSpinner();
        Button btnApplyFilter = findViewById(R.id.btnApplyFilter);
        btnApplyFilter.setOnClickListener(v -> applyFilter());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterType.setAdapter(adapter);

        spinnerFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle spinner selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void applyFilter() {
        int selectedFilter = spinnerFilterType.getSelectedItemPosition();
        switch (selectedFilter) {
            case 0: // Top Performance
                fetchQuizData();
                spinnerSpecificFilter.setVisibility(View.GONE); // Hide second spinner
                break;
            // Handle other cases
        }
    }

    private void fetchQuizData() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("QuizTaken");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userScores.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.child("userID").getValue(String.class);
                    Integer scoreValue = snapshot.child("QuizTakenScore").getValue(Integer.class);
                    int score = (scoreValue != null) ? scoreValue : 0;

                    // Manually check if the userId exists in the map
                    if (!userScores.containsKey(userId)) {
                        userScores.put(userId, new ArrayList<>());
                    }
                    userScores.get(userId).add(score);
                }
                calculateMeanScoresAndDisplayTopPerformers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void calculateMeanScoresAndDisplayTopPerformers() {
        List<UserPerformance> performances = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : userScores.entrySet()) {
            String userId = entry.getKey();
            List<Integer> scores = entry.getValue();
            double meanScore = 0;
            for (Integer score : scores) {
                meanScore += score;
            }
            meanScore /= scores.size();
            performances.add(new UserPerformance(userId, meanScore));
        }

        // Sort by mean score in descending order
        Collections.sort(performances, (p1, p2) -> Double.compare(p2.getMeanScore(), p1.getMeanScore()));

        // Assign ranks and take top 3 performers
        int rank = 1;
        for (UserPerformance performance : performances) {
            performance.setRank(rank++);
        }
        List<UserPerformance> topPerformers = performances.subList(0, Math.min(3, performances.size()));

        updateRecyclerView(topPerformers);
    }

    private void updateRecyclerView(List<UserPerformance> topPerformers) {
        if (adapter == null) {
            adapter = new AnalyticsAdapter(topPerformers);
            recyclerViewAnalytics.setAdapter(adapter);
        } else {
            adapter.updateData(topPerformers);
        }
    }
}
