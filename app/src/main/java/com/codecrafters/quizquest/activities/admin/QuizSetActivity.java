package com.codecrafters.quizquest.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.AdminQuizSetAdapter;
import com.codecrafters.quizquest.models.AdminQuizSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizSetActivity extends AppCompatActivity implements QuizSetClickListener {

    private DatabaseReference databaseReference;
    private EditText adminQuizSetCatIdEditText;
    private EditText adminQuizSetNameEditText;
    private RecyclerView recyclerView;
    private ArrayList<AdminQuizSet> quizSets = new ArrayList<>();
    private AdminQuizSetAdapter adapter;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz_set);
            initializeUIComponents();
            initializeFirebaseDatabase();
            initializeRecyclerView();
            // Set the welcome text based on the category name
            setWelcomeText();
            // Get the category ID passed from CategoryManagementActivity
            categoryId = getIntent().getStringExtra("QUIZ_CATEGORY_ID");
            fetchQuizSetsFromFirebase();
            setAddQuizSetsButtonClickListener();
        } catch (Exception e){
            showToast("Error: " + e.getMessage());
        }
    }

    private void setWelcomeText() {
        try {
            // Find the TextView
            TextView adminQuizSetWelcome = findViewById(R.id.adminQuizSetHeader);

            // Get the category name from the Intent extras
            String categoryName = getIntent().getStringExtra("QUIZ_CATEGORY_NM");

            // Check if categoryName is not null
            if (categoryName != null) {
                // Set the text of the TextView using the category name
                adminQuizSetWelcome.setText("Welcome to " + categoryName + " Quiz Sets!");
            } else {
                // Handle the case where categoryName is null or not provided
                adminQuizSetWelcome.setText("Welcome to Quiz Set Area!");
            }
        } catch (Exception e) {
            showToast("Error setting welcome text: " + e.getMessage());
        }
    }


    private void initializeUIComponents() {
        try {
            adminQuizSetCatIdEditText = findViewById(R.id.adminEditQuizSetCatId);
            adminQuizSetNameEditText = findViewById(R.id.adminEditQuizSetNm);

            // Retrieve the Quiz Set name from the Extra
            String quizSetName = getIntent().getStringExtra("QUIZ_CATEGORY_NM");

            // Find the EditText for Quiz Set Category Id
            adminQuizSetCatIdEditText = findViewById(R.id.adminEditQuizSetCatId);

            // Set the Quiz Set name as the default text
            adminQuizSetCatIdEditText.setText(quizSetName);

            // Disable the EditText to make it non-editable
            adminQuizSetCatIdEditText.setEnabled(false);
        } catch (Exception e) {
            showToast("Error initializing UI components: " + e.getMessage());
        }
    }


    private void initializeFirebaseDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        checkIfTableExists();
    }

    private void initializeRecyclerView() {
        try {
            recyclerView = findViewById(R.id.adminQuizSetRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AdminQuizSetAdapter(QuizSetActivity.this, quizSets, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            showToast("Error initializing RecyclerView: " + e.getMessage());
        }
    }


    private void fetchQuizSetsFromFirebase() {
        String categoryId = this.categoryId;

        try {
            DatabaseReference quizSetsRef = databaseReference.child("QuizSet");
            quizSetsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    quizSets.clear(); // Clear the existing list

                    try {
                        for (DataSnapshot quizSetSnapshot : dataSnapshot.getChildren()) {
                            AdminQuizSet quizSet = quizSetSnapshot.getValue(AdminQuizSet.class);
                            if (quizSet != null && categoryId.equals(quizSet.getQuizCatID())) {
                                // Add only the quiz sets that belong to the specified category
                                quizSets.add(quizSet);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        showToast("Error loading quiz sets: " + e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Database error: " + databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            showToast("Error loading quiz sets: " + e.getMessage());
        }
    }


    private void setAddQuizSetsButtonClickListener() {
        try {
            Button addQuizSetButton = findViewById(R.id.adminQuizSetAddBtn);
            addQuizSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleAddQuizSetButtonClick();
                }
            });
        } catch (Exception e) {
            showToast("Error setting click listener for addQuizSetButton: " + e.getMessage());
        }
    }


    private void handleAddQuizSetButtonClick() {
        try{
            String quizCatID = getIntent().getStringExtra("QUIZ_CATEGORY_ID");
            String quizSetName = adminQuizSetNameEditText.getText().toString();

            if (quizSetName.isEmpty()) {
                showToast("Quiz Set name cannot be empty.");
                return;
            }

            DatabaseReference quizSetsRef = databaseReference.child("QuizSet");
            generateUniqueQuizSetId(quizSetsRef, new QuizSetCallback() {
                @Override
                public void onQuizSetGenerated(String quizSetId) {
                    AdminQuizSet newQuizSet = new AdminQuizSet(
                            quizCatID,
                            quizSetId,
                            quizSetName,
                            System.currentTimeMillis(),
                            "Admin",
                            System.currentTimeMillis(),
                            "Admin"
                    );

                    DatabaseReference newQuizSetRef = quizSetsRef.child(quizSetId);
                    newQuizSetRef.setValue(newQuizSet)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showToast("Quiz Set added successfully!");
                                    clearInputFields();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("Error adding Quiz Set: " + e.getMessage());
                                }
                            });
                }

                @Override
                public void onError(String errorMessage) {
                    showToast("Error: " + errorMessage);
                }
            });
        }catch (Exception e) {
            showToast("Error loading quiz categories: " + e.getMessage());
        }
    }


    // Other methods for checking table existence, displaying toasts, etc.
    // Generate a unique Quiz Set Id based on the latest count of total items
    private void generateUniqueQuizSetId(DatabaseReference quizSetsRef, final QuizSetCallback callback) {
        try {
            String baseQuizSetId = "quizSet";
            // Query the database to get the latest count
            quizSetsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long latestCount = dataSnapshot.getChildrenCount();
                    // Create the unique quizSetId by concatenating the baseQuizSetId and the latest count
                    String uniqueQuizSetId = baseQuizSetId + (latestCount + 1);
                    // Callback with the generated unique quizSetId
                    callback.onQuizSetGenerated(uniqueQuizSetId);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if the query is canceled
                    callback.onError(databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            // Handle the error if the query is canceled
            callback.onError(e.getMessage());
        }
    }


    private void clearInputFields() {
        try {
//            adminQuizSetCatIdEditText.getText().clear();
            adminQuizSetNameEditText.getText().clear();
        } catch (Exception e) {
            showToast("Error clearing input fields: " + e.getMessage());
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Handle the edit button click
    @Override
    public void onEditQuizSetClick(int position) {
        try {
            // Handle your edit logic here.
            AdminQuizSet quizSetToEdit = quizSets.get(position);

            Dialog editDialog = createEditDialog();
            // Find the TextView for the headline
            TextView editQuizSetHeadline = editDialog.findViewById(R.id.editQuizSetHeadline);

            // Set the headline text to indicate that you are editing a specific quiz set
            editQuizSetHeadline.setText("Editing " + quizSetToEdit.getQuizSetName());

            EditText adminEditQuizSetCatId = editDialog.findViewById(R.id.editQuizCatId);
            EditText adminEditQuizSetNm = editDialog.findViewById(R.id.editQuizSetNm);

            adminEditQuizSetCatId.setText(quizSetToEdit.getQuizCatID());
            adminEditQuizSetNm.setText(quizSetToEdit.getQuizSetName());

            setSaveButtonClickListener(quizSetToEdit, editDialog, adminEditQuizSetCatId, adminEditQuizSetNm);

            editDialog.show();
        } catch (Exception e) {
            // Handle any unexpected exceptions here.
            showToast("Unexpected error: " + e.getMessage());
        }
    }


    private Dialog createEditDialog() {
        try {
            Dialog editDialog = new Dialog(QuizSetActivity.this);
            editDialog.setContentView(R.layout.edit_quiz_set_dialog);

            Window window = editDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(layoutParams);
            }

            return editDialog;
        } catch (Exception e) {
            showToast("Error creating edit dialog: " + e.getMessage());
            return null;
        }
    }



    private void setSaveButtonClickListener(AdminQuizSet quizSetToEdit, Dialog editDialog,
                                            EditText editQuizSetName, EditText editQuizSetDescription) {
        try {
            Button saveButton = editDialog.findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String updatedQuizSetName = editQuizSetName.getText().toString();
                    String updatedQuizSetDescription = editQuizSetDescription.getText().toString();

                    updateQuizSetInFirebase(quizSetToEdit, updatedQuizSetName, updatedQuizSetDescription);
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
        } catch (Exception e) {
            showToast("Error updating Quiz Set: " + e.getMessage());
        }
    }


    private void updateQuizSetInFirebase(AdminQuizSet quizSetToEdit, String updatedQuizSetName,
                                         String updatedQuizSetDescription) {
        try {
            DatabaseReference quizSetRef = databaseReference.child("QuizSet").child(quizSetToEdit.getQuizSetId());

            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("quizSetName", updatedQuizSetName);
            updatedData.put("quizSetDesc", updatedQuizSetDescription);

            quizSetRef.updateChildren(updatedData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Quiz Set updated successfully!");
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Error updating Quiz Set: " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            showToast("Error updating Quiz Set: " + e.getMessage());
        }
    }



    // Handle the delete button click
    @Override
    public void onDeleteQuizSetClick(int position) {
        try {
            AdminQuizSet quizSetToDelete = quizSets.get(position);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            deleteQuizSetFromFirebase(quizSetToDelete);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // User canceled the delete operation
                            break;
                    }
                }
            };

            showDeleteConfirmationDialog(dialogClickListener);
        } catch (Exception e) {
            showToast("Error deleting Quiz Set: " + e.getMessage());
        }
    }

    private void deleteQuizSetFromFirebase(AdminQuizSet quizSetToDelete) {
        try {
            DatabaseReference quizSetRef = databaseReference.child("QuizSet").child(quizSetToDelete.getQuizSetId());

            quizSetRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showToast("Quiz Set deleted successfully!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast("Error deleting quiz set: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showToast("Error deleting Quiz Set: " + e.getMessage());
        }
    }


    private void showDeleteConfirmationDialog(DialogInterface.OnClickListener dialogClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this Quiz Set?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    private void showAddQuizSetConfirmationDialog(DialogInterface.OnClickListener dialogClickListener) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Add Quiz Set For this Quiz Set?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    private void checkIfTableExists() {
        try {
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
        } catch (Exception e) {
            showToast("Database error: " + e.getMessage());
        }
    }

    private void createTableWithDemoData() {
        try {
            DatabaseReference categoriesRef = databaseReference.child("QuizSet");

            for (int i = 1; i <= 5; i++) {
                String quizCatID = "quizCateg1";
                String quizSetId = "quizSet" + i;
                String quizSetName = "quizSet" + i;

                AdminQuizSet newQuizSet = new AdminQuizSet(
                        quizCatID,
                        quizSetId,
                        quizSetName,
                        System.currentTimeMillis(),
                        "Admin",
                        System.currentTimeMillis(),
                        "Admin"
                );

                DatabaseReference newQuizSetRef = categoriesRef.child(quizSetId);
                newQuizSetRef.setValue(newQuizSet);
            }

            showToast("QuizCategories table created with demo data!");
        } catch (Exception e) {
            showToast("Error creating QuizCategories table: " + e.getMessage());
        }
    }

    private interface QuizSetCallback {
        void onQuizSetGenerated(String quizSetId);
        void onError(String errorMessage);
    }
}