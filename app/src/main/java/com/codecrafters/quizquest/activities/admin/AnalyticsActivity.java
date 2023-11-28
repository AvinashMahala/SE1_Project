package com.codecrafters.quizquest.activities.admin;

import android.os.Bundle;
import android.util.Log;
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
    private Map<String, String> userIdToNameMap = new HashMap<>();
    private List<String> quizCategories = new ArrayList<>();

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
        fetchUserNames();
        fetchQuizCategories();
    }

    private void fetchUserNames() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserINFO");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    String userName = snapshot.child("UserFname").getValue(String.class);
                    userIdToNameMap.put(userId, userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void fetchQuizCategories() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("QuizCategories");
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizCategories.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String category = categorySnapshot.getKey();
                    quizCategories.add(category);
                }
                setupCategorySpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterType.setAdapter(adapter);

        spinnerFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Top Performance
                    spinnerSpecificFilter.setVisibility(View.GONE);
                    fetchQuizData(null);
                } else {
                    spinnerSpecificFilter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, quizCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecificFilter.setAdapter(categoryAdapter);

        spinnerSpecificFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = quizCategories.get(position);
                fetchQuizData(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void applyFilter() {
        int selectedFilter = spinnerFilterType.getSelectedItemPosition();
        if (selectedFilter == 1) { // Top Category Performance
            String selectedCategory = (String) spinnerSpecificFilter.getSelectedItem();
            fetchQuizData(selectedCategory);
        }
    }

    private void fetchQuizData(String category) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("QuizTaken");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userScores.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String quizCategory = snapshot.child("quizCategory").getValue(String.class);
                    if (category == null || category.equals(quizCategory)) {
                        String userId = snapshot.child("userId").getValue(String.class);
                        Integer scoreValue = snapshot.child("quizTakenScore").getValue(Integer.class);
                        int score = (scoreValue != null) ? scoreValue : 0;

                        if (!userScores.containsKey(userId)) {
                            userScores.put(userId, new ArrayList<>());
                        }
                        userScores.get(userId).add(score);
                    }
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
            String userName = userIdToNameMap.get(userId);
            if (userName == null) {
                Log.e("AnalyticsActivity", "User name not found for userId: " + userId);
                continue;
            }
            List<Integer> scores = entry.getValue();
            double totalScore = 0;
            for (Integer score : scores) {
                totalScore += score;
            }
            final double meanScore = totalScore / scores.size();

            performances.add(new UserPerformance(userName, meanScore, 0)); // Rank will be assigned later
        }

        assignRanksAndDisplay(performances);
    }

    private void assignRanksAndDisplay(List<UserPerformance> performances) {
        Collections.sort(performances, (p1, p2) -> Double.compare(p2.getMeanScore(), p1.getMeanScore()));

        for (int i = 0; i < performances.size(); i++) {
            performances.get(i).setRank(i + 1);
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
