package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizCategory;

import java.util.ArrayList;

public class QuizCategoryAdapter extends RecyclerView.Adapter<QuizCategoryAdapter.viewHolder> {

    Context context;
    ArrayList<QuizCategory> arrayCategory;


    public QuizCategoryAdapter(Context context, ArrayList<QuizCategory> arrayCategory ) {
        this.context = context;
        this.arrayCategory = arrayCategory;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_quiz_categories,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.imgCategory.setImageResource(arrayCategory.get(position).getQuizCatImg());
        holder.txtName.setText(arrayCategory.get(position).getQuizCatName());

    }

    @Override
    public int getItemCount() {
        return arrayCategory.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        ImageView imgCategory;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.categoryName);
            imgCategory = itemView.findViewById(R.id.profile_image);

        }
    }
}
