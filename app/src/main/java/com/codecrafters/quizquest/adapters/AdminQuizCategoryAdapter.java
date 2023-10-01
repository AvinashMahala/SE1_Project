package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.models.AdminQuizCategory;
import com.codecrafters.quizquest.R;
import android.widget.Toast;

import java.util.List;

public class AdminQuizCategoryAdapter extends RecyclerView.Adapter<AdminQuizCategoryAdapter.ViewHolder> {

    private Context context;
    private List<AdminQuizCategory> adminQuizCategories;



    public AdminQuizCategoryAdapter(Context context, List<AdminQuizCategory> adminQuizCategories) {
        this.context = context;
        this.adminQuizCategories = adminQuizCategories;
    }

    // Add a setter method to update the list of quiz categories
    public void setQuizCategories(List<AdminQuizCategory> quizCategories) {
        this.adminQuizCategories = quizCategories;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            AdminQuizCategory adminQuizCategory = adminQuizCategories.get(position);

            holder.categoryName.setText(adminQuizCategory.getQuizCatNm());
            holder.categoryDescription.setText(adminQuizCategory.getQuizCatDesc());

            // Add click listener for each category here if needed
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle category item click
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            showToast("Error loading category data: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return adminQuizCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView categoryDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.adminCategoryName);
            categoryDescription = itemView.findViewById(R.id.categoryDescription);
        }
    }



}
