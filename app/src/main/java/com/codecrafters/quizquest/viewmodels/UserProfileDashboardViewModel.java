package com.codecrafters.quizquest.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codecrafters.quizquest.models.User;
import com.codecrafters.quizquest.models.QuizCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDashboardViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<List<QuizCategory>> quizHistory = new MutableLiveData<>();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<QuizCategory>> getQuizHistory() {
        return quizHistory;
    }

    public void loadUserData(String userId) {
        DatabaseReference userRef = db.getReference("UserINFO").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("UserFname").getValue(String.class);
                    user.setValue(new User(userName, 0)); // Initialize quizzesTaken as 0, update it later
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error here
            }
        });
    }

    public void loadQuizHistoryData(String userId) {
        DatabaseReference quizTakenRef = db.getReference("QuizTaken");
        quizTakenRef.orderByChild("UserID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<QuizCategory> fetchedQuizHistory = new ArrayList<>();
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    String quizCatId = quizSnapshot.child("QuizCatID").getValue(String.class);
                    int quizScore = quizSnapshot.child("QuizTakenScore").getValue(Integer.class);

                    // Fetch quiz category details from QuizCategories using quizCatId
                    DatabaseReference quizCatRef = db.getReference("QuizCategories").child(quizCatId);
                    quizCatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot quizCatSnapshot) {
                            if (quizCatSnapshot.exists()) {
                                String quizCatName = quizCatSnapshot.child("quizCatName").getValue(String.class); // Update field name
                                QuizCategory quiz = new QuizCategory(quizCatName, 0, quizCatId); // Updated constructor
                                fetchedQuizHistory.add(quiz);
                                quizHistory.setValue(fetchedQuizHistory);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error here
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error here
            }
        });
    }
}
