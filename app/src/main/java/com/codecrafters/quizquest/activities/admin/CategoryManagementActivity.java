package com.codecrafters.quizquest.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.AdminQuizCategory;
import com.codecrafters.quizquest.models.QuizCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CategoryManagementActivity extends AppCompatActivity {

    private EditText categoryNameEditText;
    private EditText categoryDescriptionEditText;
    private ListView categoryListView;
    private ArrayAdapter<String> categoryAdapter;

    private DatabaseReference databaseReference;
    private List<AdminQuizCategory> quizCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("QuizCategories");

        // Initialize UI elements
        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        categoryDescriptionEditText = findViewById(R.id.categoryDescriptionEditText);
        categoryListView = findViewById(R.id.categoryListView);

        // Initialize quizCategories list
        quizCategories = new ArrayList<>();

        // Initialize categoryAdapter for the ListView
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        categoryListView.setAdapter(categoryAdapter);

        // Set up a click listener for the ListView items
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click (e.g., editing or deleting the selected category)
                AdminQuizCategory selectedCategory = quizCategories.get(position);
                // Implement your logic here
            }
        });

        // Retrieve and display quiz categories from Firebase
        retrieveQuizCategories();
    }

    // Retrieve quiz categories from Firebase
    private void retrieveQuizCategories() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizCategories.clear();
                categoryAdapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminQuizCategory category = snapshot.getValue(AdminQuizCategory.class);
                    if (category != null) {
                        quizCategories.add(category);
                        categoryAdapter.add(category.getQuizCatNm());
                    }
                }

                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(CategoryManagementActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Add a new quiz category to Firebase
    private void addQuizCategory() {
        String categoryName = categoryNameEditText.getText().toString().trim();
        String categoryDescription = categoryDescriptionEditText.getText().toString().trim();

        if (!categoryName.isEmpty() && !categoryDescription.isEmpty()) {
            // Create a new AdminQuizCategory object
            AdminQuizCategory newCategory = new AdminQuizCategory(categoryName, categoryDescription);

            // Push the new category to Firebase
            databaseReference.push().setValue(newCategory);

            // Clear input fields
            categoryNameEditText.getText().clear();
            categoryDescriptionEditText.getText().clear();
        } else {
            Toast.makeText(this, "Please fill in both name and description fields.", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete a quiz category from Firebase
    private void deleteQuizCategory(int position) {
        // Implement your logic for deleting a quiz category
    }
}
