package com.codecrafters.quizquest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.UserPerformance;

import java.util.List;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ViewHolder> {

    private List<UserPerformance> data;

    public AnalyticsAdapter(List<UserPerformance> data) {
        this.data = data;
    }

    public void updateData(List<UserPerformance> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_performance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserPerformance performance = data.get(position);
        holder.bind(performance);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRank, txtUserName, txtScore;

        public ViewHolder(View itemView) {
            super(itemView);
            txtRank = itemView.findViewById(R.id.txtRank);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtScore = itemView.findViewById(R.id.txtScore);
        }

        public void bind(UserPerformance performance) {
            txtRank.setText(String.valueOf(performance.getRank()));
            txtUserName.setText(performance.getUserName());
            txtScore.setText(String.valueOf(performance.getScore()));
        }
    }
}
