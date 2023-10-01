package com.codecrafters.quizquest.activities.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import com.codecrafters.quizquest.R;

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
