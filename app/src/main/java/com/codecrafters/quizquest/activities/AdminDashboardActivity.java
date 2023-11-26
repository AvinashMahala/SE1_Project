package com.codecrafters.quizquest.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.activities.admin.AnalyticsActivity;
import com.codecrafters.quizquest.activities.admin.CategoryManagementActivity;
import com.codecrafters.quizquest.activities.admin.QuestionManagementActivity;
import com.codecrafters.quizquest.activities.admin.SendNotificationActivity;
import com.codecrafters.quizquest.activities.admin.UserAccountManagementActivity;
import com.codecrafters.quizquest.activities.admin.UserManagementActivity;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import android.widget.Toast;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        // Check the user's role to determine their permissions (Assuming role is retrieved from a database)
        String userRole = getUserRole(); // Implement this method to retrieve user role

        if (userRole.equals("Admin")) {
            // Show admin-specific features and functionality
            setupAdminUI();
        } else {
            // Show regular user features or display a message indicating limited access
            setupRegularUserUI();
        }
    }

    // Method to handle the "Manage Quiz Categories" button click
    public void onManageCategoriesClick(View view) {
        // Implement the logic to navigate to the category management screen
        try {
            // Implement the logic to navigate to the category management screen
            Intent intent = new Intent(this, CategoryManagementActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            // Log the exception using Android's Log class
            Log.e("NavigationError", "Failed to navigate to CategoryManagementActivity", e);
            // Optionally, show a user-friendly message to inform them about the issue
            Toast.makeText(this, "Error navigating to category management.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLogoutClick(View v) {
        mAuth.signOut(); // Sign out from Firebase Authentication
        Intent intent = new Intent(AdminDashboardActivity.this, CommonLoginRegistrationActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Method to handle the "Manage Quiz Questions" button click
    public void onManageQuestionsClick(View view) {
        // Implement the logic to navigate to the question management screen
        Intent intent = new Intent(this, QuestionManagementActivity.class);
        startActivity(intent);
    }

    // Method to handle the "Manage User Accounts" button click
    public void onManageUsersClick(View view) {
        try {
            // Implement the logic to navigate to the category management screen
            Intent intent = new Intent(this, UserManagementActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            // Log the exception using Android's Log class
            Log.e("NavigationError", "Failed to navigate to UserManagementActivity", e);
            // Optionally, show a user-friendly message to inform them about the issue
            Toast.makeText(this, "Error navigating to category management.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle the "Analytics and Reporting" button click
    public void onAnalyticsClick(View view) {
        // Implement the logic to navigate to the analytics and reporting screen
        Intent intent = new Intent(this, AnalyticsActivity.class);
        startActivity(intent);
    }

    private void setupAdminUI() {
        // Implement code to display admin-specific UI elements and features
    }

    private void setupRegularUserUI() {
        // Implement code to display UI elements and features for regular users
    }

    // Implement the method to retrieve the user's role from a database
    private String getUserRole() {
        // Replace this with your actual logic to fetch the user's role
        return "Admin"; // For testing purposes, assuming the user is an admin
    }
    public void onSendNotificationClick(View view) {
        Intent intent = new Intent(this, SendNotificationActivity.class);
        startActivity(intent);
    }
}