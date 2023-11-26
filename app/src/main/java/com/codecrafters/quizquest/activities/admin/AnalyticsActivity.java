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
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    private Spinner spinnerFilterType, spinnerSpecificFilter;
    private RecyclerView recyclerViewAnalytics;
    private AnalyticsAdapter adapter;

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
                fetchTopPerformers();
                break;
            // Handle other cases
        }
    }

    private void fetchTopPerformers() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("QuizTaken");
        dbRef.orderByChild("score").limitToLast(3)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<UserPerformance> topPerformers = new ArrayList<>();
                        int rank = 1;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserPerformance performance = snapshot.getValue(UserPerformance.class);
                            if (performance != null) {
                                performance.setRank(rank++);
                                topPerformers.add(performance);
                            }
                        }
                        Collections.reverse(topPerformers);
                        updateRecyclerView(topPerformers);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void updateRecyclerView(List<UserPerformance> data) {
        if (adapter == null) {
            adapter = new AnalyticsAdapter(data);
            recyclerViewAnalytics.setAdapter(adapter);
        } else {
            adapter.updateData(data);
        }
    }
}
