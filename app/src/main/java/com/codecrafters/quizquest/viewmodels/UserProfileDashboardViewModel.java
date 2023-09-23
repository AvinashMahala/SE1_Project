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
        DatabaseReference userRef = db.getReference("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User fetchedUser = dataSnapshot.getValue(User.class);
                user.setValue(fetchedUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error here
            }
        });
    }

    public void loadQuizHistoryData(String userId) {
        DatabaseReference quizHistoryRef = db.getReference("quizHistory").child(userId);
        quizHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<QuizCategory> fetchedQuizHistory = new ArrayList<>();
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    QuizCategory quiz = quizSnapshot.getValue(QuizCategory.class);
                    fetchedQuizHistory.add(quiz);
                }
                quizHistory.setValue(fetchedQuizHistory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error here
            }
        });
    }
}
