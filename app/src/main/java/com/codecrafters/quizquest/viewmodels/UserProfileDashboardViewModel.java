package com.codecrafters.quizquest.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codecrafters.quizquest.models.QuizHistoryItem;
import com.codecrafters.quizquest.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserProfileDashboardViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<List<QuizHistoryItem>> quizHistory = new MutableLiveData<>();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<QuizHistoryItem>> getQuizHistory() {
        return quizHistory;
    }

    public void loadUserData(String userId) {
        DatabaseReference userRef = db.getReference("UserINFO").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("UserFname").getValue(String.class);
                    user.setValue(new User(userName, 0));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void loadQuizHistoryData(String userId) {
        DatabaseReference quizTakenRef = db.getReference("QuizTaken");
        quizTakenRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<QuizHistoryItem> fetchedQuizHistory = new ArrayList<>();
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    String quizId = quizSnapshot.child("quizId").getValue(String.class);
                    int quizScore = quizSnapshot.child("quizTakenScore").getValue(Integer.class);
                    String date = quizSnapshot.child("quizTakenOn").getValue(String.class);
                    fetchedQuizHistory.add(new QuizHistoryItem(quizId, quizScore, date));
                }
                quizHistory.setValue(fetchedQuizHistory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}