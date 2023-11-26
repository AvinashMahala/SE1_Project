package com.codecrafters.quizquest.activities.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.adapters.AdminUserAdapter;
import com.codecrafters.quizquest.models.AdminUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserManagementActivity extends AppCompatActivity implements AdminUserAdapter.AdminUserClickListener {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<AdminUser> adminUserList;
    private AdminUserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_management);

        initializeUIComponents();
        initializeFirebaseDatabase();
        fetchUsers();
    }

    private void initializeUIComponents() {
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adminUserList = new ArrayList<>();

        adapter = new AdminUserAdapter(UserManagementActivity.this, adminUserList, this);



        recyclerView.setAdapter(adapter);
    }

    private void initializeFirebaseDatabase() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void fetchUsers() {
        DatabaseReference userRef = databaseReference.child("UserINFO");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminUserList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    AdminUser user = userSnapshot.getValue(AdminUser.class);
                    if (user != null) {
                        user.setUserID(userSnapshot.getKey());
                        Log.d("onDataChange", "UserID: " + userSnapshot.getKey());
                        adminUserList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    @Override
    public void onUserEditClick(AdminUser user) {
        //AdminUser userToEdit = adminUserList.get(position);
        showEditUserDialog(user);
    }

    @Override
    public void onUserEditClick(int position) {

    }

    @Override
    public void onUserViewClick(AdminUser user) {
        showUserDetailsDialog(user);
    }

    @Override
    public void onUserDeleteClick(AdminUser user) {
        showDeleteConfirmationDialog(user);
    }

    private interface AdminUserClickListener {
        void onUserClick(AdminUser user);
    }
    private void showUserDetailsDialog(AdminUser user) {
        final Dialog userDetailsDialog = new Dialog(this);
        userDetailsDialog.setContentView(R.layout.dialog_view_user);

        Window window = userDetailsDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams); }


        TextView viewUserId = userDetailsDialog.findViewById(R.id.viewUserId);
        TextView viewUserPhNo = userDetailsDialog.findViewById(R.id.viewUserPhNo);
        TextView viewUserFname = userDetailsDialog.findViewById(R.id.viewUserFname);
        TextView viewUserEmail = userDetailsDialog.findViewById(R.id.viewUserEmail);
        TextView viewUserDOB = userDetailsDialog.findViewById(R.id.viewUserDOB);
        TextView viewUserRole = userDetailsDialog.findViewById(R.id.viewUserRole);
        TextView viewUserCreatedOn = userDetailsDialog.findViewById(R.id.viewUserCreatedOn);
        TextView viewUserCreatedBy = userDetailsDialog.findViewById(R.id.viewUserCreatedBy);
        TextView viewUserModifiedOn = userDetailsDialog.findViewById(R.id.viewUserModifiedOn);
        TextView viewUserModifiedBy = userDetailsDialog.findViewById(R.id.viewUserModifiedBy);

        viewUserId.setText(user.getUserID());
        viewUserPhNo.setText(user.getUserPhNo());
        viewUserFname.setText(user.getUserFname());
        viewUserEmail.setText(user.getUserEmail());
        viewUserDOB.setText(user.getUserDOB()); // Assuming it's already formatted correctly
        viewUserRole.setText(user.getUserRole());
        //viewUserCreatedOn.setText(formatTimestamp(user.getUserCreatedOn()));
        //viewUserCreatedBy.setText(user.getUserCreatedBy());
        //viewUserModifiedOn.setText(formatTimestamp(user.getUserModifiedOn()));
        //viewUserModifiedBy.setText(user.getUserModifiedBy());

        Button closeViewButton = userDetailsDialog.findViewById(R.id.closeViewButton);
        closeViewButton.setOnClickListener(v -> userDetailsDialog.dismiss());

        userDetailsDialog.show();
    }
    private void showEditUserDialog(AdminUser user) {
        final Dialog editUserDialog = new Dialog(this);
        editUserDialog.setContentView(R.layout.dialog_edit_user);

        EditText editUserId = editUserDialog.findViewById(R.id.editUserId);
        EditText editUserPhNo = editUserDialog.findViewById(R.id.editUserPhNo);
        EditText editUserFname = editUserDialog.findViewById(R.id.editUserFname);
        EditText editUserEmail = editUserDialog.findViewById(R.id.editUserEmail);
        EditText editUserDOB = editUserDialog.findViewById(R.id.editUserDOB);
        EditText editUserRole = editUserDialog.findViewById(R.id.editUserRole);

        // Set current user data in fields
        editUserId.setText(user.getUserID());
        editUserId.setEnabled(false);
        editUserPhNo.setText(user.getUserPhNo());
        editUserFname.setText(user.getUserFname());
        editUserEmail.setText(user.getUserEmail());
        editUserDOB.setText(user.getUserDOB());
        editUserRole.setText(user.getUserRole());

        Button saveButton = editUserDialog.findViewById(R.id.saveEditButton);
        saveButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the save button if any field has been changed
                boolean isChanged = !editUserPhNo.getText().toString().equals(user.getUserPhNo()) ||
                        !editUserFname.getText().toString().equals(user.getUserFname()) ||
                        !editUserEmail.getText().toString().equals(user.getUserEmail()) ||
                        !editUserDOB.getText().toString().equals(user.getUserDOB()) ||
                        !editUserRole.getText().toString().equals(user.getUserRole());
                saveButton.setEnabled(isChanged);
            }


            @Override
            public void afterTextChanged(Editable s) {}
        };
        //editUserId.addTextChangedListener(textWatcher);
        editUserPhNo.addTextChangedListener(textWatcher);
        editUserFname.addTextChangedListener(textWatcher);
        editUserEmail.addTextChangedListener(textWatcher);
        editUserDOB.addTextChangedListener(textWatcher);
        editUserRole.addTextChangedListener(textWatcher);

        editUserId.setText(user.getUserID());
        editUserPhNo.setText(user.getUserPhNo());
        editUserFname.setText(user.getUserFname());
        editUserEmail.setText(user.getUserEmail());
        editUserDOB.setText(user.getUserDOB());
        editUserRole.setText(user.getUserRole());





        saveButton.setOnClickListener(v -> {

            //String newUserId = editUserId.getText().toString();
            String newUserPhNo = editUserPhNo.getText().toString();
            String newUserFname = editUserFname.getText().toString();
            String newUserEmail = editUserEmail.getText().toString();
            String newUserDOB = editUserDOB.getText().toString();
            String newUserRole = editUserRole.getText().toString();


            // Update user data in Firebase or local list
            updateUserInFirebase(user, newUserPhNo, newUserFname, newUserEmail, newUserDOB, newUserRole);
            editUserDialog.dismiss();
        });
        Button cancelButton = editUserDialog.findViewById(R.id.cancelEditButton);
        cancelButton.setOnClickListener(v -> editUserDialog.dismiss());

        editUserDialog.show();


    }
    private void updateUserInFirebase(AdminUser user, String userPhNo, String userFname, String userEmail, String userDOB, String userRole) {
        if (user.getUserID() == null || user.getUserID().isEmpty()) {
            Toast.makeText(this, "Error: User ID is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = databaseReference.child("UserINFO").child(user.getUserID());
        Log.d("UpdateUser", "Updating at path: " + userRef.getParent());
        Log.d("UpdateUser", "UserID: " + user.getUserID());
        Log.d("UpdateUser", "UserFname: " + user.getUserFname());
        Log.d("UpdateUser", "UserEmail: " + user.getUserEmail());
        Log.d("UpdateUser", "UserDOB: " + user.getUserDOB());




        // Prepare the data to update
        Map<String, Object> userData = new HashMap<>();
        userData.put("UserPhNo", userPhNo);
        userData.put("UserFname", userFname);
        userData.put("UserEmail", userEmail);
        userData.put("UserDOB", userDOB);
        userData.put("UserRole", userRole);

        Log.d("UpdateUser", "UserDOB: " + userData);


        userRef.updateChildren(userData)
                .addOnSuccessListener(aVoid -> Toast.makeText(UserManagementActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(UserManagementActivity.this, "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }




    private void setUserStatus(AdminUser user, String status) {
        if (user.getUserID() == null || user.getUserID().isEmpty()) {
            Toast.makeText(this, "Error: User ID is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = databaseReference.child("UserINFO").child(user.getUserID());
        userRef.child("UserStatus").setValue(status)
                .addOnSuccessListener(aVoid -> Toast.makeText(UserManagementActivity.this, "User status set to " + status + " successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(UserManagementActivity.this, "Failed to set user status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showDeleteConfirmationDialog(AdminUser user) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to set this user as inactive?")
                .setPositiveButton("Yes", (dialog, which) -> setUserStatus(user, "Inactive")) // Setting user as inactive
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
