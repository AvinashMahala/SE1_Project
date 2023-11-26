package com.codecrafters.quizquest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codecrafters.quizquest.R;
import com.codecrafters.quizquest.models.AdminUser;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.AdminUserViewHolder> {

    private Context context;
    private List<AdminUser> adminUserList;
    private AdminUserClickListener listener; // Listener for edit and delete actions

    public AdminUserAdapter(Context context, List<AdminUser> adminUserList, AdminUserClickListener listener) {
        this.context = context;
        this.adminUserList = adminUserList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false);
        return new AdminUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserViewHolder holder, int position) {
        AdminUser currentUser = adminUserList.get(position);
        holder.userNameTextView.setText(currentUser.getUserFname()); // Assuming you want to display the user's first name

        // Set up the view button click listener
        holder.viewButton.setOnClickListener(v -> {
            if(listener != null) {
                listener.onUserViewClick(currentUser);
            }
        });

        holder.editButton.setOnClickListener(v -> {
            if(listener != null) {
                listener.onUserEditClick(currentUser);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if(listener != null) {
                listener.onUserDeleteClick(currentUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminUserList.size();
    }

    public class AdminUserViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView;
        Button viewButton, editButton, deleteButton;

        public AdminUserViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.adminUserName);
            viewButton = itemView.findViewById(R.id.viewUserButton);
            editButton = itemView.findViewById(R.id.editUserButton);
            deleteButton = itemView.findViewById(R.id.deleteUserButton);
        }
    }

    public interface AdminUserClickListener {
        void onUserEditClick(AdminUser user);

        void onUserEditClick(int position);

        void onUserViewClick(AdminUser user);
        void onUserDeleteClick(AdminUser user);
    }
}
