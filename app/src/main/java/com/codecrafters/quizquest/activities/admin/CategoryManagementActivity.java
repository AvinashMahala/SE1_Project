package com.codecrafters.quizquest.activities.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.adapters.AdminQuizCategoryAdapter;
import com.codecrafters.quizquest.adapters.QuizCategoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.AdminQuizCategory;

public class CategoryManagementActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Check if "QuizCategories" table exists
        checkIfTableExists();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.quizCategoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Fetch quiz categories from Firebase and populate the adapter
        DatabaseReference quizCategoriesRef = databaseReference.child("QuizCategories");

        quizCategoriesRef.addValueEventListener(new ValueEventListener() {
            // Fetch quiz categories from Firebase and populate the adapter
            ArrayList<AdminQuizCategory> quizCategories = new ArrayList<>(); // Replace with your actual data retrieval logic
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizCategories.clear(); // Clear the existing list

                try {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        // Get values from the snapshot
                        String quizCatID = (String) categorySnapshot.child("QuizCatID").getValue();
                        String quizCatNm = (String) categorySnapshot.child("QuizCatNm").getValue();
                        String quizCatDesc = (String) categorySnapshot.child("QuizCatDesc").getValue();
                        String quizCateImgUrl = (String) categorySnapshot.child("QuizCateImgUrl").getValue();
                        long quizCatCreatedOn = (long) categorySnapshot.child("QuizCatCreatedOn").getValue();
                        String quizCatCreatedBy = (String) categorySnapshot.child("QuizCatCreatedBy").getValue();
                        long quizCatModifiedOn = (long) categorySnapshot.child("QuizCatModifiedOn").getValue();
                        String quizCatModifiedBy = (String) categorySnapshot.child("QuizCatModifiedBy").getValue();

                        // Create an AdminQuizCategory object
                        AdminQuizCategory category = new AdminQuizCategory(
                                quizCatID, quizCatNm, quizCatDesc, quizCateImgUrl,
                                quizCatCreatedOn, quizCatCreatedBy, quizCatModifiedOn,
                                quizCatModifiedBy
                        );

                        // Add the AdminQuizCategory object to your list
                        quizCategories.add(category);
                    }

                    // Create an instance of your adapter and set it to the RecyclerView
                    AdminQuizCategoryAdapter adapter = new AdminQuizCategoryAdapter(CategoryManagementActivity.this, quizCategories);
                    adapter.setQuizCategories(quizCategories);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exceptions here, e.g., show an error message to the user
                    showToast("Error loading quiz categories: " + e.getMessage());
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Database error: " + databaseError.getMessage());
            }
        });
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void checkIfTableExists() {
        databaseReference.child("QuizCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // If the "QuizCategories" table does not exist, create it and add demo data
                    createTableWithDemoData();
                } else {
                    showToast("QuizCategories table already exists.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Database error: " + databaseError.getMessage());
            }
        });
    }

    private void createTableWithDemoData() {
        // Initialize "QuizCategories" table with 5 demo entries
        for (int i = 1; i <= 5; i++) {
            String categoryId = "quizCateg" + i;
            String categoryName = "Demo Category " + i;
            String categoryDescription = "Description for Demo Category " + i;
            String imageUrl = "https://example.com/image" + i + ".jpg";
            long createdOn = System.currentTimeMillis(); // Timestamp of creation
            String createdBy = "Admin";
            long modifiedOn = System.currentTimeMillis(); // Timestamp of modification
            String modifiedBy = "Admin";

            // Create a Map with the data
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("QuizCatID", categoryId);
            categoryData.put("QuizCatNm", categoryName);
            categoryData.put("QuizCatDesc", categoryDescription);
            categoryData.put("QuizCateImgUrl", imageUrl);
            categoryData.put("QuizCatCreatedOn", createdOn);
            categoryData.put("QuizCatCreatedBy", createdBy);
            categoryData.put("QuizCatModifiedOn", modifiedOn);
            categoryData.put("QuizCatModifiedBy", modifiedBy);

            // Add the data to the database
            databaseReference.child("QuizCategories").child(categoryId).setValue(categoryData);
        }
        showToast("QuizCategories table created with demo data!");
    }
}
