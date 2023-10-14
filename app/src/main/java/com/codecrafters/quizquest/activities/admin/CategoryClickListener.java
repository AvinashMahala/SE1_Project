package com.codecrafters.quizquest.activities.admin;

public interface CategoryClickListener {
    void onEditClick(int position);
    void onDeleteClick(int position);
    void onQuizSetCategoryButtonClick(int position);
}
