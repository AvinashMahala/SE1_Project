package com.codecrafters.quizquest.activities.admin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.AdminQuizCategory;

public class CategoryManagementActivity extends AppCompatActivity implements CategoryClickListener {

    private DatabaseReference databaseReference;
    private EditText categoryNameEditText;
    private EditText categoryDescriptionEditText;
    private Spinner sortingOptionsSpinner; // Added Spinner for sorting options


    private RecyclerView recyclerView;
    private ArrayList<AdminQuizCategory> quizCategories = new ArrayList<>();
    private AdminQuizCategoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);
        initializeUIComponents();
        initializeFirebaseDatabase();
        initializeRecyclerView();
        fetchQuizCategoriesFromFirebase();
        setAddCategoryButtonClickListener();

        // Initialize and set up the Spinner for sorting options
        sortingOptionsSpinner = findViewById(R.id.sortingOptionsSpinner);
        setupSortingOptionsSpinner();
    }

    // ...

    private void setupSortingOptionsSpinner() {
        Map<String, String> sortingOptions = new HashMap<>();
        sortingOptions.put("a_sort_by_hint", "Sort Category By");
        sortingOptions.put("b_last_modified", "By Last Modified On");
        sortingOptions.put("c_category_name", "Category Name");

        // Create an ArrayAdapter using the sortingOptions map
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                R.layout.admin_quiz_cat_spinner_drpdn_item_hint, // Use the custom layout
                new ArrayList<>(sortingOptions.values())
        );

        // Add a hint as the first item
//        spinnerAdapter.insert("Sort Category By", 0);

        // Set the adapter for the Spinner
        sortingOptionsSpinner.setAdapter(spinnerAdapter);

        // Set a listener for item selection
        sortingOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected sorting option key
                String[] keys = sortingOptions.keySet().toArray(new String[0]);
                String selectedOptionKey = keys[position];

                // Handle the selected sorting option (e.g., sort by name or last modified)
                handleSortingOptionSelected(selectedOptionKey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });
    }


    private void handleSortingOptionSelected(String selectedOptionKey) {
        // Handle the selected sorting option here
        // You can implement sorting logic based on the selected key
        // For example, if selectedOptionKey is "last_modified", sort by last modified timestamp,
        // and if selectedOptionKey is "category_name", sort by category name.

        switch (selectedOptionKey) {
            case "b_last_modified":
                // Sort by last modified timestamp (ascending)
                Collections.sort(quizCategories, new Comparator<AdminQuizCategory>() {
                    @Override
                    public int compare(AdminQuizCategory category1, AdminQuizCategory category2) {
                        return Long.compare(category1.getQuizCatModifiedOn(), category2.getQuizCatModifiedOn());
                    }
                });
                break;

            case "c_category_name":
                // Sort by category name (ascending)
                Collections.sort(quizCategories, new Comparator<AdminQuizCategory>() {
                    @Override
                    public int compare(AdminQuizCategory category1, AdminQuizCategory category2) {
                        return category1.getQuizCatNm().compareToIgnoreCase(category2.getQuizCatNm());
                    }
                });
                break;

            // Add more cases for other sorting options if needed

            default:
                // Handle the default case (no sorting or reset to original order)
                break;
        }

        // Notify the adapter of the data change
        adapter.notifyDataSetChanged();
    }


    private void initializeUIComponents() {
        categoryNameEditText = findViewById(R.id.adminEditCategoryName);
        categoryDescriptionEditText = findViewById(R.id.adminEditCategoryDesc);
    }

    private void initializeFirebaseDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        checkIfTableExists();
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.quizCategoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminQuizCategoryAdapter(CategoryManagementActivity.this, quizCategories, this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchQuizCategoriesFromFirebase() {
        DatabaseReference quizCategoriesRef = databaseReference.child("QuizCategories");
        quizCategoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizCategories.clear(); // Clear the existing list

                try {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        AdminQuizCategory category = categorySnapshot.getValue(AdminQuizCategory.class);
                        if (category != null) {
                            quizCategories.add(category);
                        }
                    }

                    // Sort the quizCategories list based on last modified timestamp
                    Collections.sort(quizCategories);

                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    showToast("Error loading quiz categories: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Database error: " + databaseError.getMessage());
            }
        });
    }

    private void setAddCategoryButtonClickListener() {
        Button addCategoryButton = findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddCategoryButtonClick();
            }
        });
    }

    private void handleAddCategoryButtonClick() {
        String categoryName = categoryNameEditText.getText().toString();
        String categoryDescription = categoryDescriptionEditText.getText().toString();

        if (categoryName.isEmpty()) {
            showToast("Category name cannot be empty.");
            return;
        }

        DatabaseReference categoriesRef = databaseReference.child("QuizCategories");
        generateUniqueCategoryId(categoriesRef, new CategoryCallback() {
            @Override
            public void onCategoryGenerated(String categoryId) {
                AdminQuizCategory newCategory = new AdminQuizCategory(
                        categoryId,
                        categoryName,
                        categoryDescription,
                        null,
                        System.currentTimeMillis(),
                        "Admin",
                        System.currentTimeMillis(),
                        "Admin"
                );

                DatabaseReference newCategoryRef = categoriesRef.child(categoryId);
                newCategoryRef.setValue(newCategory)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showToast("Category added successfully!");
                                clearInputFields();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showToast("Error adding category: " + e.getMessage());
                            }
                        });
            }

            @Override
            public void onError(String errorMessage) {
                showToast("Error: " + errorMessage);
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

    private void clearInputFields() {
        categoryNameEditText.getText().clear();
        categoryDescriptionEditText.getText().clear();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Handle the edit button click
    @Override
    public void onEditClick(int position) {
        try {
            // Handle your edit logic here.
            AdminQuizCategory categoryToEdit = quizCategories.get(position);

            Dialog editDialog = createEditDialog();
            // Find the TextView for the headline
            TextView editCategoryHeadline = editDialog.findViewById(R.id.editCategoryHeadline);

            // Set the headline text to indicate that you are editing a specific category
            editCategoryHeadline.setText("Editing " + categoryToEdit.getQuizCatNm());

            EditText editCategoryName = editDialog.findViewById(R.id.editCategoryName);
            EditText editCategoryDescription = editDialog.findViewById(R.id.editCategoryDescription);

            editCategoryName.setText(categoryToEdit.getQuizCatNm());
            editCategoryDescription.setText(categoryToEdit.getQuizCatDesc());

            setSaveButtonClickListener(categoryToEdit, editDialog, editCategoryName, editCategoryDescription);

            editDialog.show();
        } catch (Exception e) {
            // Handle any unexpected exceptions here.
            showToast("Unexpected error: " + e.getMessage());
        }
    }

    private Dialog createEditDialog() {
        Dialog editDialog = new Dialog(CategoryManagementActivity.this);
        editDialog.setContentView(R.layout.edit_category_dialog);

        Window window = editDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }

        return editDialog;
    }

    private void setSaveButtonClickListener(AdminQuizCategory categoryToEdit, Dialog editDialog,
                                            EditText editCategoryName, EditText editCategoryDescription) {
        Button saveButton = editDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedCategoryName = editCategoryName.getText().toString();
                String updatedCategoryDescription = editCategoryDescription.getText().toString();

                updateCategoryInFirebase(categoryToEdit, updatedCategoryName, updatedCategoryDescription);
                editDialog.dismiss();
            }
        });
        // Handle the "Cancel" button click
        Button cancelButton = editDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the edit dialog to cancel the editing operation.
                editDialog.dismiss();
            }
        });
    }

    private void updateCategoryInFirebase(AdminQuizCategory categoryToEdit, String updatedCategoryName,
                                          String updatedCategoryDescription) {
        DatabaseReference categoryRef = databaseReference.child("QuizCategories").child(categoryToEdit.getQuizCatID());

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("quizCatNm", updatedCategoryName);
        updatedData.put("quizCatDesc", updatedCategoryDescription);

        categoryRef.updateChildren(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("Category updated successfully!");
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error updating category: " + e.getMessage());
                    }
                });
    }

    // Handle the delete button click
    @Override
    public void onDeleteClick(int position) {
        AdminQuizCategory categoryToDelete = quizCategories.get(position);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteCategoryFromFirebase(categoryToDelete);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // User canceled the delete operation
                        break;
                }
            }
        };

        showDeleteConfirmationDialog(dialogClickListener);
    }

    // Handle the onQuizSetCategoryButtonClick button click
    @Override
    public void onQuizSetCategoryButtonClick(int position) {
        AdminQuizCategory selectedCategory = quizCategories.get(position);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Get the category ID associated with the clicked item
                        String categoryId = selectedCategory.getQuizCatID();

                        // Create an Intent to start the Quiz Set activity
                        Intent intent = new Intent(getApplicationContext(), QuizSetActivity.class);

                        // Add the category ID as an extra to the Intent
                        intent.putExtra("QUIZ_CATEGORY_ID", categoryId);
                        intent.putExtra("QUIZ_CATEGORY_NM", selectedCategory.getQuizCatNm());

                        // Start the Quiz Set activity
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // User canceled the delete operation
                        break;
                }
            }
        };

        showAddQuizSetConfirmationDialog(dialogClickListener);
    }

    private void deleteCategoryFromFirebase(AdminQuizCategory categoryToDelete) {
        DatabaseReference categoryRef = databaseReference.child("QuizCategories").child(categoryToDelete.getQuizCatID());

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
    }

    private void showDeleteConfirmationDialog(DialogInterface.OnClickListener dialogClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    private void showAddQuizSetConfirmationDialog(DialogInterface.OnClickListener dialogClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Add Quiz Set For this category?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    private void checkIfTableExists() {
        databaseReference.child("QuizCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
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
        DatabaseReference categoriesRef = databaseReference.child("QuizCategories");

        for (int i = 1; i <= 5; i++) {
            String categoryId = "quizCateg" + i;
            String categoryName = "Demo Category " + i;
            String categoryDescription = "Description for Demo Category " + i;

            AdminQuizCategory newCategory = new AdminQuizCategory(
                    categoryId,
                    categoryName,
                    categoryDescription,
                    "https://example.com/image" + i + ".jpg",
                    System.currentTimeMillis(),
                    "Admin",
                    System.currentTimeMillis(),
                    "Admin"
            );

            DatabaseReference newCategoryRef = categoriesRef.child(categoryId);
            newCategoryRef.setValue(newCategory);
        }

        showToast("QuizCategories table created with demo data!");
    }

    private interface CategoryCallback {
        void onCategoryGenerated(String categoryId);

        void onError(String errorMessage);
    }
}
