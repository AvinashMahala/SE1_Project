package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizTaken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    Context context;
    ArrayList<QuizTaken> list;

    public LeaderBoardAdapter(Context context, ArrayList<QuizTaken> list) {
        this.context = context;
        this.list = list;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.leaderboarditem, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder, int position) {
        QuizTaken qt = list.get(position);
        String uid = qt.getUserId();

        // Fetch username asynchronously
        fetchUsernameFromFirebase(uid, new UsernameCallback() {
            @Override
            public void onUsernameFetched(String username) {
                holder.name.setText(username);
            }
        });

        int score = qt.getQuizTakenScore();
        holder.score.setText(String.valueOf(score));
        holder.rank.setText(String.valueOf(position + 1)); // Position is zero-based, so add 1 for the rank
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rank, name, score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewUserName);
            rank = itemView.findViewById(R.id.textViewRank);
            score = itemView.findViewById(R.id.textViewScore);
        }
    }

    private void fetchUsernameFromFirebase(String uid, UsernameCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("UserINFO");
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("UserFname").getValue(String.class);

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
