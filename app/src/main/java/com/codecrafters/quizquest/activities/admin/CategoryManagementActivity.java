package com.codecrafters.quizquest.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.adapters.AdminQuizCategoryAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private EditText categoryNameEditText;
    private EditText categoryDescriptionEditText;

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
                        String quizCatID = (String) categorySnapshot.child("quizCatID").getValue();
                        String quizCatNm = (String) categorySnapshot.child("quizCatNm").getValue();
                        String quizCatDesc = (String) categorySnapshot.child("quizCatDesc").getValue();
                        String quizCateImgUrl = (String) categorySnapshot.child("quizCateImgUrl").getValue();
                        long quizCatCreatedOn = (long) categorySnapshot.child("quizCatCreatedOn").getValue();
                        String quizCatCreatedBy = (String) categorySnapshot.child("quizCatCreatedBy").getValue();
                        long quizCatModifiedOn = (long) categorySnapshot.child("quizCatModifiedOn").getValue();
                        String quizCatModifiedBy = (String) categorySnapshot.child("quizCatModifiedBy").getValue();

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

        // Initialize UI components
        categoryNameEditText = findViewById(R.id.adminEditCategoryName);
        categoryDescriptionEditText = findViewById(R.id.adminEditCategoryDesc);

        // Listen for the "Add Category" button click
        Button addCategoryButton = findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get user input for the new category
                    String categoryName = categoryNameEditText.getText().toString();
                    String categoryDescription = categoryDescriptionEditText.getText().toString();
                    // Get other category data as needed

                    // Check if input is valid (e.g., not empty)
                    if (categoryName.isEmpty()) {
                        showToast("Category name cannot be empty.");
                        return;
                    }

                    // Generate a unique quizCatID based on the latest count of total items
                    DatabaseReference categoriesRef = databaseReference.child("QuizCategories");
                    generateUniqueCategoryId(categoriesRef, new CategoryCallback() {
                        @Override
                        public void onCategoryGenerated(String categoryId) {
                            // Create a new AdminQuizCategory object with the generated ID
                            AdminQuizCategory newCategory = new AdminQuizCategory(
                                    categoryId,
                                    categoryName,
                                    categoryDescription,
                                    null, // Set imageUrl to null for now or provide an image URL
                                    System.currentTimeMillis(), // Timestamp of creation
                                    "Admin", // Created by
                                    System.currentTimeMillis(), // Timestamp of modification
                                    "Admin" // Modified by
                            );

                            // Push the new category data to Firebase Realtime Database
                            DatabaseReference newCategoryRef = categoriesRef.child(categoryId);

                            newCategoryRef.setValue(newCategory)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Category added successfully
                                            showToast("Category added successfully!");
                                            clearInputFields(); // Clear input fields
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle category addition failure
                                            showToast("Error adding category: " + e.getMessage());
                                        }
                                    });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Handle the error message
                            showToast("Error: " + errorMessage);
                        }
                    });

//
//                    // Push the new category data to Firebase Realtime Database
//                    DatabaseReference newCategoryRef = categoriesRef.child(newCategoryId);
//
//                    newCategoryRef.setValue(newCategory)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    // Category added successfully
//                                    showToast("Category added successfully!");
//                                    clearInputFields(); // Clear input fields
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // Handle category addition failure
//                                    showToast("Error adding category: " + e.getMessage());
//                                }
//                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exceptions here, e.g., show an error message to the user
                    showToast("Error adding category: " + e.getMessage());
                }
            }
        });
    }

    // Other methods for checking table existence, displaying toasts, etc.

    // Generate a unique categoryId based on the latest count of total items
    private void generateUniqueCategoryId(DatabaseReference categoriesRef, final CategoryCallback callback) {
        String baseCategoryId = "quizCateg";

        // Query the database to get the latest count
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long latestCount = dataSnapshot.getChildrenCount();

                // Create the unique categoryId by concatenating the baseCategoryId and the latest count
                String uniqueCategoryId = baseCategoryId + (latestCount + 1);

                // Callback with the generated unique categoryId
                callback.onCategoryGenerated(uniqueCategoryId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if the query is canceled
                callback.onError(databaseError.getMessage());
            }
        });
    }

    // Define a callback interface to handle the generated categoryId
    private interface CategoryCallback {
        void onCategoryGenerated(String categoryId);

        void onError(String errorMessage);
    }




    private void clearInputFields() {
        categoryNameEditText.getText().clear();
        categoryDescriptionEditText.getText().clear();
        // Clear other input fields if needed
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
            categoryData.put("quizCatID", categoryId);
            categoryData.put("quizCatNm", categoryName);
            categoryData.put("quizCatDesc", categoryDescription);
            categoryData.put("quizCateImgUrl", imageUrl);
            categoryData.put("quizCatCreatedOn", createdOn);
            categoryData.put("quizCatCreatedBy", createdBy);
            categoryData.put("quizCatModifiedOn", modifiedOn);
            categoryData.put("quizCatModifiedBy", modifiedBy);

            // Add the data to the database
            databaseReference.child("QuizCategories").child(categoryId).setValue(categoryData);
        }
        showToast("QuizCategories table created with demo data!");
    }
}
