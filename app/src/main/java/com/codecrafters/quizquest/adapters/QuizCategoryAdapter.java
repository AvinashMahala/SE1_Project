package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.activities.RecyclerViewInterface;
import com.codecrafters.quizquest.models.QuizCategory;

import java.util.ArrayList;

public class QuizCategoryAdapter extends RecyclerView.Adapter<QuizCategoryAdapter.viewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<QuizCategory> arrayCategory;



    public QuizCategoryAdapter(Context context, ArrayList<QuizCategory> arrayCategory, RecyclerViewInterface recyclerViewInterface ) {
        this.context = context;
        this.arrayCategory = arrayCategory;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_quiz_categories,parent,false);
        return new viewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        QuizCategory quizCategory = arrayCategory.get(position);
        Glide.with(context).load(arrayCategory.get(position).getQuizCatImg()).into(holder.imgCategory);
        holder.txtName.setText(quizCategory.getQuizCatName());

    }

    @Override
    public int getItemCount() {
        return arrayCategory.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        ImageView imgCategory;
        public viewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            txtName = itemView.findViewById(R.id.categoryName);
            imgCategory = itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onQuizCategoryClick(pos);
                        }
                    }
                }
            });

        }
    }
}
