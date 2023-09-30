package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codecrafters.quizquest.R;

import com.codecrafters.quizquest.models.AdminQuizCategory;
import java.util.List;

public class AdminQuizCategoryAdapter extends RecyclerView.Adapter<AdminQuizCategoryAdapter.ViewHolder> {

    private Context context;
    private List<AdminQuizCategory> adminQuizCategories;

    public AdminQuizCategoryAdapter(Context context, List<AdminQuizCategory> adminQuizCategories) {
        this.context = context;
        this.adminQuizCategories = adminQuizCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminQuizCategory adminQuizCategory = adminQuizCategories.get(position);

        holder.txtName.setText(adminQuizCategory.getQuizCatName());
        // You can bind additional data as needed for admin functionality.
    }

    @Override
    public int getItemCount() {
        return adminQuizCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.categoryListView);
        }
    }
}
