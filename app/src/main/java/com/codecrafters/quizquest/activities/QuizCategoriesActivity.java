package com.codecrafters.quizquest.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.QuizCategoryAdapter;
import com.codecrafters.quizquest.models.AdminQuizCategory;
import com.codecrafters.quizquest.models.QuizCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class QuizCategoriesActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<QuizCategory> arrCategory = new ArrayList<>();
    DatabaseReference db1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_grid);
        RecyclerView rc = findViewById(R.id.recycleList);

        db1 = FirebaseDatabase.getInstance().getReference();

        DatabaseReference db = db1.child("QuizCategories");
        rc.setHasFixedSize(true);

        rc.setLayoutManager(new GridLayoutManager(this, 2));

        QuizCategoryAdapter adapter = new QuizCategoryAdapter(this, arrCategory, this);
        rc.setAdapter(adapter);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String quizCatName = (String) dataSnapshot.child("quizCatNm").getValue();
                   // String quizCatImg = (String) dataSnapshot.child("quizCatImg").getValue();
                    String catKey = (String) dataSnapshot.child("quizCatID").getValue();
                    String quizCatImg = "https://firebasestorage.googleapis.com/v0/b/codecrafters-quizquest.appspot.com/o/astronomy.png?alt=media&token=2601d765-635c-49d9-80d1-214eff9a8390&_gl=1*1kmocji*_ga*MTc4OTIzNzQ5OS4xNjg0Njc9*ga_CW55HF8NVT*MTY5NjIwMDY1OC4xNi4xLjE2OTYyMDA2NjUuNTMuMC4w";

                    QuizCategory quizCategory=new QuizCategory(quizCatName, quizCatImg, catKey);

                    arrCategory.add(quizCategory);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onQuizCategoryClick(int position) {
        String catKey = arrCategory.get(position).getCatKey();

        Intent intent = new Intent(QuizCategoriesActivity.this, QuizDashboardActivity.class);
        intent.putExtra("catKey", catKey);
        startActivity(intent);

    }
}
