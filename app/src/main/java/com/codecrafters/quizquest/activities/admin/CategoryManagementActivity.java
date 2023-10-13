package com.codecrafters.quizquest.activities.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class CategoryManagementActivity extends AppCompatActivity implements CategoryClickListener{

    private DatabaseReference databaseReference;
    private EditText categoryNameEditText;
    private EditText categoryDescriptionEditText;

    private RecyclerView recyclerView;
    private ArrayList<AdminQuizCategory> quizCategories = new ArrayList<>();
    private AdminQuizCategoryAdapter adapter;

    @Override
    public void onEditClick(int position) {
        try {
            // Handle your edit logic here.
            AdminQuizCategory categoryToEdit = quizCategories.get(position);

            // Create an edit dialog
            Dialog editDialog = new Dialog(CategoryManagementActivity.this);

// Set the custom layout for the dialog
            editDialog.setContentView(R.layout.edit_category_dialog);

// Set the width and height of the dialog (adjust these values as needed)
            Window window = editDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());

                // Set the width and height here (e.g., 80% of the screen width and height)
                layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Adjust the height as needed

                window.setAttributes(layoutParams);
            }

// Now, you can show the dialog
            editDialog.show();


            // Initialize EditText fields and set their initial values
            EditText editCategoryName = editDialog.findViewById(R.id.editCategoryName);
            EditText editCategoryDescription = editDialog.findViewById(R.id.editCategoryDescription);

            // Set initial values based on the selected category
            editCategoryName.setText(categoryToEdit.getQuizCatNm());
            editCategoryDescription.setText(categoryToEdit.getQuizCatDesc());

            // Handle the save button click
            Button saveButton = editDialog.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Get updated values from EditText fields
                        String updatedCategoryName = editCategoryName.getText().toString();
                        String updatedCategoryDescription = editCategoryDescription.getText().toString();

                        // Update the category details
                        DatabaseReference categoryRef = databaseReference.child("QuizCategories").child(categoryToEdit.getQuizCatID());

                        // Create a Map to store the updated data
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("quizCatNm", updatedCategoryName);
                        updatedData.put("quizCatDesc", updatedCategoryDescription);

                        // Use updateChildren to update specific fields in Firebase
                        categoryRef.updateChildren(updatedData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Category updated successfully
                                        showToast("Category updated successfully!");

                                        // Notify the adapter to refresh the data in the RecyclerView
                                        adapter.notifyDataSetChanged();

                                        // Dismiss the edit dialog
                                        editDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle update failure
                                        showToast("Error updating category: " + e.getMessage());
                                    }
                                });

                    } catch (Exception e) {
                        // Handle any unexpected exceptions here.
                        showToast("Unexpected error: " + e.getMessage());
                    }
                }
            });


            // Show the edit dialog
            editDialog.show();
        } catch (Exception e) {
            // Handle any unexpected exceptions here.
            showToast("Unexpected error: " + e.getMessage());
        }
    }


    @Override
    public void onDeleteClick(int position) {
        try {
            // Handle your delete logic here. For this demo, we'll show a toast and delete from Firebase:
            showToast("Delete clicked for position: " + position);
            AdminQuizCategory categoryToRemove = adapter.getQuizCategoryAtPosition(position);

            if (categoryToRemove != null && categoryToRemove.getQuizCatID() != null) {
                DatabaseReference categoryRef = databaseReference.child("QuizCategories").child(categoryToRemove.getQuizCatID());

                categoryRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("Category deleted successfully!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error deleting category: " + e.getMessage());
                    }
                });
            } else {
                showToast("Error: Invalid category or ID");
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions here.
            showToast("Unexpected error: " + e.getMessage());
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        recyclerView = findViewById(R.id.quizCategoryRecyclerView);  // Moved inside onCreate
        adapter = new AdminQuizCategoryAdapter(CategoryManagementActivity.this, quizCategories, this);  // Moved inside onCreate


        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Check if "QuizCategories" table exists
        checkIfTableExists();

        // Initialize RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch quiz categories from Firebase and populate the adapter
        DatabaseReference quizCategoriesRef = databaseReference.child("QuizCategories");
        quizCategories = new ArrayList<>();

        quizCategoriesRef.addValueEventListener(new ValueEventListener() {
            // Fetch quiz categories from Firebase and populate the adapter

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
                    //adapter = new AdminQuizCategoryAdapter(CategoryManagementActivity.this, quizCategories, this);
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
