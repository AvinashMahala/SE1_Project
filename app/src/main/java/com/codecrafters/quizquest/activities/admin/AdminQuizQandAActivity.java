package com.codecrafters.quizquest.activities.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.activities.AddQuizQuestionActivity;
import com.codecrafters.quizquest.adapters.QuizQuestionAdapter;
import com.codecrafters.quizquest.models.QuizQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminQuizQandAActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private final ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
    private QuizQuestionAdapter adapter;
    public String quizSetId, categoryId;

    public AdminQuizQandAActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qanda);

        quizSetId = getIntent().getStringExtra("QUIZ_SET_ID");
        categoryId = getIntent().getStringExtra("QUIZ_CATEGORY_ID");
        Button addQuestionButton = findViewById(R.id.adminQuizQuestionAddBtn);
        initializeUIComponents();
        initializeFirebaseDatabase();
        fetchQuizQuestions();
        Intent intent = getIntent();

        addQuestionButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(AdminQuizQandAActivity.this, AddQuizQuestionActivity.class);
            intent1.putExtra("QUIZ_CATEGORY_ID", categoryId); // assuming you have categoryId
            intent1.putExtra("SET_ID", quizSetId); // assuming you have setId
            startActivity(intent1);
        });
    }


    private void initializeUIComponents() {
        RecyclerView recyclerView = findViewById(R.id.recycleList);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            // Correctly initialize the adapter here
            adapter = new QuizQuestionAdapter(AdminQuizQandAActivity.this, quizQuestions);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("AdminQuizQandAActivity", "RecyclerView not found in layout");
        }
    }

    private void initializeFirebaseDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void fetchQuizQuestions() {
        DatabaseReference quizQuestionsRef = databaseReference.child("QuizQuestions");
        quizQuestionsRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizQuestions.clear();
                if (!dataSnapshot.exists()) {
                    Log.e("AdminQuizQandAActivity", "No questions found for quizSetId: " + quizSetId);
                    updateUIBasedOnQuestions();
                    return;
                }
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String questionId = questionSnapshot.getKey();
                    if (questionId != null && questionId.startsWith(categoryId + "_" + quizSetId + "_")) {
                        QuizQuestion question = questionSnapshot.getValue(QuizQuestion.class);
                        if (question != null) {
                            quizQuestions.add(question);
                        } else {
                            Log.e("AdminQuizQandAActivity", "Error reading question at: " + questionSnapshot.getKey());
                        }
                    }
                    updateUIBasedOnQuestions();
                }

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminQuizQandAActivity", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    private void updateUIBasedOnQuestions() {
        if (quizQuestions.isEmpty()) {
            findViewById(R.id.emptyWatermark).setVisibility(View.VISIBLE);
            findViewById(R.id.recycleList).setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyWatermark).setVisibility(View.GONE);
            findViewById(R.id.recycleList).setVisibility(View.VISIBLE);
        }
    }



}
