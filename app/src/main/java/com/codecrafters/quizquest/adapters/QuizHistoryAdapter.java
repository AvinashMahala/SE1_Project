package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizCategory;

import java.util.ArrayList;

public class QuizHistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QuizCategory> quizHistory;

    public QuizHistoryAdapter(Context context, ArrayList<QuizCategory> quizHistory) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_quiz_categories, parent, false);
        }

        TextView categoryName = convertView.findViewById(R.id.categoryName);
        ImageView categoryImage = convertView.findViewById(R.id.profile_image);

        QuizCategory currentQuiz = quizHistory.get(position);

        categoryName.setText(currentQuiz.getQuizCatName());
        categoryImage.setImageResource(currentQuiz.getQuizCatImg());

        return convertView;
    }
}
