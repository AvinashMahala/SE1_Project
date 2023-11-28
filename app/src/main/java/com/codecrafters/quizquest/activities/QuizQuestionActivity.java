package com.codecrafters.quizquest.activities;

import com.codecrafters.quizquest.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codecrafters.quizquest.models.QuizQuestion;
import com.codecrafters.quizquest.models.QuizTaken;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class QuizQuestionActivity extends AppCompatActivity {
    private TextView questionText;
    private RadioGroup optionsGroup;
    private Chronometer timer;
    private Button nextButton, endQuizButton, skipQuestionButton;

    // Create an ArrayList to store QuizQuestion models
    private ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
    private int currentQuestion = 0;
    private int score = 0;

    private String userId;
    private String quizCatId;
    private String quizSetId;
    private String quizTakenOn;
    private String userModifiedBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        timer = findViewById(R.id.timer);
        nextButton = findViewById(R.id.nextButton);
        endQuizButton = findViewById(R.id.endQuizButton);
        skipQuestionButton = findViewById(R.id.skipQuestionButton);
       // mAuth = FirebaseAuth.getInstance();
       // FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = "5dKVjXejbGQg8JlhQ2wtqMyC42H2"; //currentUser.getUid();


         quizCatId = getIntent().getStringExtra("quizCat");
         quizSetId = getIntent().getStringExtra("quizSet");
         userModifiedBy = "admin";
        // Get the current date
        Date currentDate = new Date();

        // Format the date as "MM/dd/yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        quizTakenOn = dateFormat.format(currentDate);

       // Initialize and start the timer for 30 minutes
        long initialTime = SystemClock.elapsedRealtime() + (30 * 60 * 1000); // 30 minutes in milliseconds
        timer.setBase(initialTime);
        timer.start();


        // Fetch data from Firebase and populate the ArrayList
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("QuizQuestions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizQuestions.clear(); // Clear the list to avoid duplicates

                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String questionId = questionSnapshot.getKey();
                  //  if (questionId != null && questionId.startsWith(quizCatId + "_" + quizSetId + "_")) {

                         QuizQuestion quizQuestion = questionSnapshot.getValue(QuizQuestion.class);
                         quizQuestions.add(quizQuestion);

                   //  }

                }
                showQuestion(currentQuestion);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, if any
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestion < quizQuestions.size() - 1) {
                    currentQuestion++;
                    showQuestion(currentQuestion);
                } else {
                    // Display a dialog indicating it's the last question
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestionActivity.this);
                    builder.setTitle("Last Question")
                            .setMessage("You are already at the last question.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Do nothing or add any additional action needed
                                }
                            });
                    builder.create().show();
                }
            }
        });

        endQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestionActivity.this);
                builder.setTitle("End Quiz Confirmation")
                        .setMessage("Are you sure you want to end the quiz?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User confirmed, navigate to QuizResultsPage
                                postQuizResultsToDB();
                                Intent intent = new Intent(QuizQuestionActivity.this, QuizResultsPage.class);
                                intent.putExtra("score", score);
                                intent.putExtra("quizCat", quizCatId);
                                intent.putExtra("quizSet", quizSetId);
                                quizSetId = getIntent().getStringExtra("quizSet");
                             //   intent.putExtra("")// Pass the score variable to the intent
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User canceled, do nothing or add any additional action needed
                            }
                        });
                builder.create().show();
            }
        });

        skipQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestion < quizQuestions.size() - 1) {
                    currentQuestion++;
                    showQuestion(currentQuestion);
                } else {
                    // Display a dialog indicating it's the last question
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizQuestionActivity.this);
                    builder.setTitle("Last Question")
                            .setMessage("You are already at the last question.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Do nothing or add any additional action needed
                                }
                            });
                    builder.create().show();
                }
            }
        });

    }


    private void showQuestion(int questionIndex) {

        if (questionIndex < quizQuestions.size()) {
            QuizQuestion quizQuestion = quizQuestions.get(questionIndex);
            questionText.setText(quizQuestion.getQuizQuestion());
            String[] answerOptions = {
                    quizQuestion.getQuizQuesAnsA(),
                    quizQuestion.getQuizQuesAnsB(),
                    quizQuestion.getQuizQuesAnsC(),
                    quizQuestion.getQuizQuesAnsD()
            };

            optionsGroup.setOnCheckedChangeListener(null); // Remove previous listener
            optionsGroup.clearCheck(); // Clear previous selection

            optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int selectedAnswerIndex = group.indexOfChild(group.findViewById(checkedId));
                    int ans = charToNumber(quizQuestion.getQuizAnswerKey());


                    if (selectedAnswerIndex + 1 == ans) {
                        // Correct answer
                        score++;
                    }

                }
            });
            for (int i = 0; i < 4; i++) {
                RadioButton radioButton = (RadioButton) optionsGroup.getChildAt(i);
                if (i < answerOptions.length) {
                    radioButton.setText(answerOptions[i]);
                } else {
                    radioButton.setText(""); // Clear any extra options if they don't exist
                }
            }
        } else {
            // Handle the case when there are no more questions
            // You can show a message or take appropriate action here
        }
    }


    private int charToNumber(String c) {
        char character = c.charAt(0);
        switch (character) {
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            default:
                throw new IllegalArgumentException("Input is not in the range 'a' to 'd'");
        }


    }


    private void postQuizResultsToDB() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("QuizTaken");

        // Create a unique key for the new QuizTaken entry
        String quizTakenId = databaseReference.push().getKey();

        // Create a new QuizTaken object
        QuizTaken quizTaken = new QuizTaken(quizTakenId,userId,quizCatId,score,quizSetId,quizTakenOn,userModifiedBy);

        // Push the QuizTaken object to the database
        databaseReference.child(quizTakenId).setValue(quizTaken);

        // You can add a completion listener to know if the data has been successfully written
        databaseReference.child(quizTakenId).setValue(quizTaken, (databaseError, databaseReference1) -> {
            if (databaseError != null) {
                // Handle the error
                Log.e("Firebase", "Data could not be saved: " + databaseError.getMessage());
            } else {
                // Data saved successfully
                Log.d("Firebase", "Data saved successfully!");
            }
        });
    }
}
