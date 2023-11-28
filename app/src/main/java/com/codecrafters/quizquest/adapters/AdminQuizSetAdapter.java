package com.codecrafters.quizquest.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.activities.admin.AdminQuizQandAActivity;
import com.codecrafters.quizquest.activities.admin.QuizSetClickListener;
import com.codecrafters.quizquest.models.AdminQuizSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminQuizSetAdapter extends RecyclerView.Adapter<AdminQuizSetAdapter.ViewHolder> {

    private final Context context;
    private List<AdminQuizSet> adminQuizSets;
    private final QuizSetClickListener listener;
    private String categoryId;

    public AdminQuizSetAdapter(Context context, List<AdminQuizSet> adminQuizSets, QuizSetClickListener listener, String categoryId) {
        this.context = context;
        this.adminQuizSets = adminQuizSets;
        this.listener = listener;
        this.categoryId = categoryId;
    }
    public AdminQuizSet getQuizSetAtPosition(int position) {
        return adminQuizSets.get(position);
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setQuizSets(List<AdminQuizSet> quizSets) {
        this.adminQuizSets = quizSets;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_set, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            AdminQuizSet adminQuizSet = adminQuizSets.get(position);

//            holder.adminEditQuizSetCatId.setText(adminQuizSet.getQuizCatID());
            holder.adminEditQuizSetNm.setText(adminQuizSet.getQuizSetName());

            // Add click listener for each quiz set here if needed
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle quiz set item click
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            // Handle any potential exceptions here
            showToast("Error loading quiz set data: " + e.getMessage());
        }
    }
    @Override
    public int getItemCount() {
        return adminQuizSets.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView adminEditQuizSetCatId;
        TextView adminEditQuizSetNm;
        Button editButton, deleteButton, QandAButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            adminEditQuizSetCatId = itemView.findViewById(R.id.adminEditQuizSetCatId);
            adminEditQuizSetNm = itemView.findViewById(R.id.adminQuizSetName);
            editButton = itemView.findViewById(R.id.editQuizSetButton);
            deleteButton = itemView.findViewById(R.id.deleteQuizSetButton);
            QandAButton = itemView.findViewById(R.id.qandaButton);

            QandAButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            AdminQuizSet selectedQuizSet = adminQuizSets.get(position);
                            String quizSetId = selectedQuizSet.getQuizSetId();


                            Intent intent = new Intent(context, AdminQuizQandAActivity.class);
//                            String categoryId = getIntent().getStringExtra("QUIZ_CATEGORY_ID");
                            intent.putExtra("QUIZ_CATEGORY_ID", categoryId);
                            intent.putExtra("QUIZ_SET_ID", quizSetId);
                            Log.d("AdminQuizSetAdapter", "Received categoryId: " + categoryId);
                            context.startActivity(intent);
                        }
                    }
                }
            });


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditQuizSetClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteQuizSetClick(position);
                        }
                    }
                }
            });
        }
    }
    private void deleteQuizSet(AdminQuizSet quizSet) {
        DatabaseReference quizSetRef = FirebaseDatabase.getInstance().getReference().child("QuizSet").child(quizSet.getQuizSetId());
        quizSetRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully deleted
                Toast.makeText(context, "Quiz Set deleted successfully!", Toast.LENGTH_SHORT).show();
                // Remove the quiz set from the local list and notify the adapter
                adminQuizSets.remove(quizSet);
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to delete
                Toast.makeText(context, "Error deleting Quiz Set: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
