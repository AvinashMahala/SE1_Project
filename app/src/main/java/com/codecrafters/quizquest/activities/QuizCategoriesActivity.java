package com.codecrafters.quizquest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import android.os.Bundle;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.QuizCategoryAdapter;
import com.codecrafters.quizquest.models.QuizCategory;

import java.util.ArrayList;

public class QuizCategoriesActivity extends AppCompatActivity {

    ArrayList<QuizCategory> arrCategory = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_grid);
        RecyclerView rc = findViewById(R.id.recycleList);
        rc.setLayoutManager(new GridLayoutManager(this, 2));

        arrCategory.add( new QuizCategory("Astronomy",R.drawable.astronomy,"1"));
        arrCategory.add( new QuizCategory("Chemistry",R.drawable.chemistry,"2"));
        arrCategory.add( new QuizCategory("Geography",R.drawable.geography,"3"));
        arrCategory.add( new QuizCategory("History",R.drawable.history,"4"));
        arrCategory.add( new QuizCategory("Physics",R.drawable.physics,"5"));
        arrCategory.add( new QuizCategory("Sports",R.drawable.sports,"6"));
        arrCategory.add( new QuizCategory("Chemistry",R.drawable.chemistry,"2"));
        arrCategory.add( new QuizCategory("Geography",R.drawable.geography,"3"));
        arrCategory.add( new QuizCategory("History",R.drawable.history,"4"));
        arrCategory.add( new QuizCategory("Physics",R.drawable.physics,"5"));
        arrCategory.add( new QuizCategory("Sports",R.drawable.sports,"6"));
        QuizCategoryAdapter adapter = new QuizCategoryAdapter(this, arrCategory);
        rc.setAdapter(adapter);


    }
}
