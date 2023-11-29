package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizHistoryItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class QuizHistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuizHistoryItem> quizHistory;

    public QuizHistoryAdapter(Context context, ArrayList<QuizHistoryItem> quizHistory) {
        this.context = context;
        this.quizHistory = quizHistory;
    }

    @Override
    public int getCount() {
        return quizHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return quizHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.quiz_history_item, parent, false);
        }

        TextView quizName = convertView.findViewById(R.id.quizName);
        TextView score = convertView.findViewById(R.id.score);
        TextView totalScore = convertView.findViewById(R.id.totalScore);

        QuizHistoryItem currentQuiz = quizHistory.get(position);

        fetchUsernameFromFirebase(currentQuiz.getQuizId(), new LeaderBoardAdapter.UsernameCallback() {
            @Override
            public void onUsernameFetched(String username) {
                quizName.setText(username);
            }
        });
        quizName.setText(currentQuiz.getQuizId());
        score.setText("Score: " + currentQuiz.getScore());
        totalScore.setText("Total Score: " + 100); // Hardcoded 100 as total score for now

        return convertView;
    }

    private void fetchUsernameFromFirebase(String uid, LeaderBoardAdapter.UsernameCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("QuizCategories");
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("quizCatNm").getValue(String.class);

                    // Now you have the username
                    // You can use it as needed
                    Log.d("Firebase", "Username: " + username);

                    // Pass the username back through the callback
                    callback.onUsernameFetched(username);
                } else {
                    Log.d("Firebase", "User with UID " + uid + " does not exist");
                    callback.onUsernameFetched(null); // Or handle as needed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching username: " + databaseError.getMessage());
                callback.onUsernameFetched(null); // Or handle as needed
            }
        });
    }

    public interface UsernameCallback {
        void onUsernameFetched(String username);
    }
}
