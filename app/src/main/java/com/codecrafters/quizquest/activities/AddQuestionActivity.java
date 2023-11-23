package com.codecrafters.quizquest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codecrafters.quizquest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddQuestionActivity extends Activity {

    private Spinner categorySpinner;
    private EditText questionEditText, optionAEditText, optionBEditText, optionCEditText, optionDEditText, correctAnswerEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        categorySpinner = findViewById(R.id.categorySpinner);
        questionEditText = findViewById(R.id.questionEditText);
        optionAEditText = findViewById(R.id.optionAEditText);
        optionBEditText = findViewById(R.id.optionBEditText);
        optionCEditText = findViewById(R.id.optionCEditText);
        optionDEditText = findViewById(R.id.optionDEditText);
        correctAnswerEditText = findViewById(R.id.correctAnswerEditText);
        submitButton = findViewById(R.id.submitButton);

        fetchCategoriesFromFirebase();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuestionToFirebase();
            }
        });
    }

    private void fetchCategoriesFromFirebase() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("QuizCategories");
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                categories.add("Choose Category");  // Add placeholder as the first item

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    // Check if the value is a HashMap or a String
                    if (categorySnapshot.getValue() instanceof HashMap) {
                        HashMap<String, Object> categoryMap = (HashMap<String, Object>) categorySnapshot.getValue();
                        // Assuming the category name is stored under a key named "name"
                        String category = (String) categoryMap.get("name");
                        if (category != null) {
                            categories.add(category);
                        }
                    } else {
                        String category = categorySnapshot.getValue(String.class);
                        if (category != null) {
                            categories.add(category);
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddQuestionActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
                Toast.makeText(AddQuestionActivity.this, "Failed to fetch categories.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitQuestionToFirebase() {
        String category = categorySpinner.getSelectedItem().toString();
        String question = questionEditText.getText().toString();
        String optionA = optionAEditText.getText().toString();
        String optionB = optionBEditText.getText().toString();
        String optionC = optionCEditText.getText().toString();
        String optionD = optionDEditText.getText().toString();
        String correctAnswer = correctAnswerEditText.getText().toString();

        Question newQuestion = new Question(category, question, optionA, optionB, optionC, optionD, correctAnswer);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("QuizQuestions");
        String questionId = databaseReference.push().getKey();
        databaseReference.child(questionId).setValue(newQuestion);
    }
}

class Question {
    private String category;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private boolean isApproved;

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(Question.class)
    }

    public Question(String category, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.category = category;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.isApproved = false; // By default, the question is not approved
    }
    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
