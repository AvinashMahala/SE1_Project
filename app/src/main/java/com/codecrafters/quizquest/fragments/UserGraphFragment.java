package com.codecrafters.quizquest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.customViews.LineChartView;

public class UserGraphFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_graph, container, false);

        // Find views by their IDs within the rootView
        LineChartView lineChartView = rootView.findViewById(R.id.lineChartView);
        float[] dataPoints = {10, 20, 30, 40, 50}; // Replace with your data
        lineChartView.setDataPoints(dataPoints);
        return rootView;
    }
}
