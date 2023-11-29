package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizHistoryItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        fetchUsernameFromFirebase(currentQuiz.getQuizId(), new UsernameCallback() {
            @Override
            public void onUsernameFetched(String username) {
                quizName.setText(username);
            }
        });

        score.setText("Score: " + currentQuiz.getScore());
        totalScore.setText("Total Score: " + 100); // Hardcoded 100 as total score for now

        return convertView;
    }

    private void fetchUsernameFromFirebase(String uid, UsernameCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("QuizTaken");
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String catId = dataSnapshot.child("quizCatId").getValue(String.class);

                    // Now, fetch catnm from QuizCategories using catId
                    DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("QuizCategories");
                    categoriesRef.child(catId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot catDataSnapshot) {
                            if (catDataSnapshot.exists()) {
                                String catnm = catDataSnapshot.child("quizCatNm").getValue(String.class);

                                // Now you have the catnm
                                // You can use it as needed
                                Log.d("Firebase", "Catnm: " + catnm);

                                // Pass the catnm back through the callback
                                callback.onUsernameFetched(catnm);
                            } else {
                                Log.d("Firebase", "Category with ID " + catId + " does not exist");
                                callback.onUsernameFetched(null); // Or handle as needed
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Firebase", "Error fetching category name: " + databaseError.getMessage());
                            callback.onUsernameFetched(null); // Or handle as needed
                        }
                    });
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