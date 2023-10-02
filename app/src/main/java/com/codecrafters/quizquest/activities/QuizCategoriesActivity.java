package com.codecrafters.quizquest.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.QuizCategoryAdapter;
import com.codecrafters.quizquest.models.QuizCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class QuizCategoriesActivity extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<QuizCategory> arrCategory = new ArrayList<>();
    DatabaseReference db1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_grid);
        RecyclerView rc = findViewById(R.id.recycleList);

        db1 = FirebaseDatabase.getInstance().getReference();

        DatabaseReference db = db1.child("QuizCategory");
        rc.setHasFixedSize(true);


        rc.setLayoutManager(new GridLayoutManager(this, 2));

          //  arrCategory.add( new QuizCategory("Astronomy",R.drawable.astronomy,"1"));
          //arrCategory.add( new QuizCategory("Chemistry",R.drawable.chemistry,"2"));
          //arrCategory.add( new QuizCategory("Geography",R.drawable.geography,"3"));
          //arrCategory.add( new QuizCategory("History",R.drawable.history,"4"));
          //  arrCategory.add( new QuizCategory("Physics",R.drawable.physics,"5"));
          // arrCategory.add( new QuizCategory("Sports",R.drawable.sports,"6"));

        QuizCategoryAdapter adapter = new QuizCategoryAdapter(this, arrCategory, this);
        rc.setAdapter(adapter);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String quizCatName = (String) dataSnapshot.child("quizCatName").getValue();
                    String quizCatImg = (String) dataSnapshot.child("quizCatImg").getValue();
                    String catKey = (String) dataSnapshot.child("catKey").getValue();

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

    @Override
    public void onQuizCategoryClick(int position) {
        String quizName = arrCategory.get(position).getQuizCatName();

        Intent intent = new Intent(QuizCategoriesActivity.this, QuizDashboardActivity.class);
        intent.putExtra("quizName", quizName);
        startActivity(intent);

    }
}
