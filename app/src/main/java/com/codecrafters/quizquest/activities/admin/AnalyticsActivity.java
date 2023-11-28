package com.codecrafters.quizquest.activities.admin;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
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
        setupCategorySpinner();
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

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilterType.setAdapter(adapter);

        spinnerFilterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) { // Top Category Performance
                    spinnerSpecificFilter.setVisibility(View.VISIBLE);
                } else {
                    spinnerSpecificFilter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupCategorySpinner() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("QuizCategories");
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizCategories.clear();
                quizCategories.add("Select Category"); // Default option
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String category = categorySnapshot.getKey();
                    quizCategories.add(category);
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(AnalyticsActivity.this,
                        android.R.layout.simple_spinner_item, quizCategories);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSpecificFilter.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void applyFilter() {
        int selectedFilter = spinnerFilterType.getSelectedItemPosition();
        String selectedCategory = spinnerSpecificFilter.getSelectedItem().toString();

        if (selectedFilter == 1 && !selectedCategory.equals("Select Category")) { // Top Category Performance
            fetchQuizData(selectedCategory);
        } else if (selectedFilter == 0) { // Top Performance
            fetchQuizData(null);
        } else {
            Toast.makeText(AnalyticsActivity.this, "Please select a valid category", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchQuizData(String category) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("QuizTaken");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userScores.clear();
                boolean dataFound = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String quizCategory = snapshot.child("quizCategory").getValue(String.class);
                    if (category == null || (quizCategory != null && quizCategory.equals(category))) {
                        dataFound = true;
                        String userId = snapshot.child("userId").getValue(String.class);
                        if (userId == null || !userIdToNameMap.containsKey(userId)) {
                            continue; // Skip if userId is null or not found in the map
                        }
                        Integer scoreValue = snapshot.child("quizTakenScore").getValue(Integer.class);
                        int score = (scoreValue != null) ? scoreValue : 0;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            userScores.computeIfAbsent(userId, k -> new ArrayList<>()).add(score);
                        }
                    }
                }

                if (!dataFound && category != null) {
                    displayNoDataMessage(category);
                } else {
                    calculateMeanScoresAndDisplayTopPerformers();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void displayNoDataMessage(String category) {
        if (adapter != null) {
            adapter.updateData(new ArrayList<>()); // Clear existing data
        }
        Toast.makeText(AnalyticsActivity.this, "No quiz taken for '" + category + "' yet", Toast.LENGTH_LONG).show();
    }

    private void calculateMeanScoresAndDisplayTopPerformers() {
        List<UserPerformance> performances = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : userScores.entrySet()) {
            String userId = entry.getKey();
            String userName = userIdToNameMap.get(userId);
            if (userName == null) {
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

        int size = performances.size();
        if (size == 0) {
            displayNoDataMessage();
            return;
        }

        for (int i = 0; i < size; i++) {
            performances.get(i).setRank(i + 1);
        }

        List<UserPerformance> topPerformers = performances.subList(0, Math.min(3, size));
        updateRecyclerView(topPerformers);
    }

    private void updateRecyclerView(List<UserPerformance> topPerformers) {
        if (topPerformers.isEmpty()) {
            if (adapter != null) {
                adapter.updateData(new ArrayList<>()); // Clear existing data
            }
            Toast.makeText(AnalyticsActivity.this, "No quizzes taken yet", Toast.LENGTH_LONG).show();
            return;
        }

        if (adapter == null) {
            adapter = new AnalyticsAdapter(topPerformers);
            recyclerViewAnalytics.setAdapter(adapter);
        } else {
            adapter.updateData(topPerformers);
        }
    }

    private void displayNoDataMessage() {
        if (adapter != null) {
            adapter.updateData(new ArrayList<>()); // Clear existing data
        }
        Toast.makeText(AnalyticsActivity.this, "No one has taken the quiz yet", Toast.LENGTH_LONG).show();
    }

}
