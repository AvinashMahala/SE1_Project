package com.codecrafters.quizquest.activities.admin;

import com.codecrafters.quizquest.models.AdminUser;

public interface AdminUserClickListener {
    void onUserEditClick(AdminUser user);
    void onUserViewClick(AdminUser user);
    void onUserDeleteClick(AdminUser user);
}