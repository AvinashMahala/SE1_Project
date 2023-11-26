package com.codecrafters.quizquest.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizQuestion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddQuizQuestionActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText optionAEditText, optionBEditText, optionCEditText, optionDEditText;
    private EditText correctAnswerEditText;
    private Button saveQuestionButton;
    private DatabaseReference databaseReference;

    public String categoryId, setId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz_question);

        initializeUI();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        categoryId = getIntent().getStringExtra("QUIZ_CATEGORY_ID");
        setId = getIntent().getStringExtra("SET_ID");

//        categoryId = getIntent().getStringExtra("QUIZ_CATEGORY_ID");
        Log.d("AddQuizQuestionActivity", "Received categoryId: " + categoryId);


        saveQuestionButton.setOnClickListener(v -> saveQuestion());
    }

    private void initializeUI() {
        questionEditText = findViewById(R.id.questionEditText);
        optionAEditText = findViewById(R.id.optionAEditText);
        optionBEditText = findViewById(R.id.optionBEditText);
        optionCEditText = findViewById(R.id.optionCEditText);
        optionDEditText = findViewById(R.id.optionDEditText);
        correctAnswerEditText = findViewById(R.id.correctAnswerEditText);
        saveQuestionButton = findViewById(R.id.saveQuestionButton);
    }

    interface QuestionIdCallback {
        void onQuestionIdGenerated(String questionId);
    }


    private void saveQuestion() {
        String questionText = questionEditText.getText().toString();
        String correctAnswer = correctAnswerEditText.getText().toString();

        if (questionText.isEmpty() || correctAnswer.isEmpty() ||
                optionAEditText.getText().toString().isEmpty() ||
                optionBEditText.getText().toString().isEmpty() ||
                optionCEditText.getText().toString().isEmpty() ||
                optionDEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> options = new HashMap<>();
        options.put("QuizQuesAnsA", optionAEditText.getText().toString());
        options.put("QuizQuesAnsB", optionBEditText.getText().toString());
        options.put("QuizQuesAnsC", optionCEditText.getText().toString());
        options.put("QuizQuesAnsD", optionDEditText.getText().toString());
        options.put("QuizAnswerKey", correctAnswer);
        options.put("QuizQuestion", questionText);

        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "anonymous";
        options.put("SubmittedBy", userId);

        generateAndSaveQuestion(questionText, options, correctAnswer, new QuestionIdCallback() {
            @Override
            public void onQuestionIdGenerated(String questionId) {
                options.put("QuizQuesID", questionId);
                QuizQuestion newQuestion = new QuizQuestion();
                newQuestion.setQuestionText(questionText);
                newQuestion.setQuizQuesAnsA(optionAEditText.getText().toString());
                newQuestion.setQuizQuesAnsB(optionBEditText.getText().toString());
                newQuestion.setQuizQuesAnsC(optionCEditText.getText().toString());
                newQuestion.setQuizQuesAnsD(optionDEditText.getText().toString());
                newQuestion.setCorrectAnswer(correctAnswer);
                newQuestion.setQuestionId(questionId);

                saveNewQuestion(questionId, newQuestion, options);
            }
        });
    }



    private void generateAndSaveQuestion(String questionText, Map<String, Object> options, String correctAnswer, QuestionIdCallback callback) {
        DatabaseReference questionsRef = databaseReference.child("QuizQuestions");
        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    if (key != null && key.startsWith(categoryId + "_" + setId + "_")) {
                        count++;
                    }
                }
                String uniqueId = "ques" + (count + 1);
                Log.d("AddQuizQuestionActivity", "Using categoryId in generateAndSaveQuestion: " + categoryId);

                String questionId = categoryId + "_" + setId + "_" + uniqueId;
                callback.onQuestionIdGenerated(questionId);

                QuizQuestion newQuestion = new QuizQuestion();
                newQuestion.setQuestionText(questionText);
                newQuestion.setQuizQuesAnsA(optionAEditText.getText().toString());
                newQuestion.setQuizQuesAnsB(optionBEditText.getText().toString());
                newQuestion.setQuizQuesAnsC(optionCEditText.getText().toString());
                newQuestion.setQuizQuesAnsD(optionDEditText.getText().toString());
                newQuestion.setCorrectAnswer(correctAnswer);
                newQuestion.setQuestionId(questionId);

                saveNewQuestion(questionId, newQuestion, options);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddQuizQuestionActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveNewQuestion(String questionId, QuizQuestion newQuestion, Map<String, Object> options) {
        databaseReference.child("QuizQuestions").child(questionId).setValue(options)
                .addOnSuccessListener(aVoid -> Toast.makeText(AddQuizQuestionActivity.this, "Question added successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddQuizQuestionActivity.this, "Failed to add question: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
