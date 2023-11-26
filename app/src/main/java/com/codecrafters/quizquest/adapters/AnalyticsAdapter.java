package com.codecrafters.quizquest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.UserPerformance;
import java.util.List;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ViewHolder> {

    private List<UserPerformance> userPerformances;

    public AnalyticsAdapter(List<UserPerformance> userPerformances) {
        this.userPerformances = userPerformances;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_performance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserPerformance performance = userPerformances.get(position);
        holder.userNameTextView.setText(performance.getUserName());
        holder.averageScoreTextView.setText(String.format("Avg Score: %.2f", performance.getMeanScore()));
        holder.rankTextView.setText(String.format("Rank: %d", performance.getRank()));
    }


    @Override
    public int getItemCount() {
        return userPerformances.size();
    }

    public void updateData(List<UserPerformance> newUserPerformances) {
        userPerformances.clear();
        userPerformances.addAll(newUserPerformances);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView averageScoreTextView;
        TextView rankTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.txtUserName);
            averageScoreTextView = itemView.findViewById(R.id.txtScore);
            rankTextView = itemView.findViewById(R.id.txtRank);
        }
    }
}
