package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.QuizHistoryItem;

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

        quizName.setText(currentQuiz.getQuizId());
        score.setText("Score: " + currentQuiz.getScore());
        totalScore.setText("Total Score: " + 100); // Hardcoded 100 as total score for now

        return convertView;
    }
}
