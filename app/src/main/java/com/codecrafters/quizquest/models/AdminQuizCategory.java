package com.codecrafters.quizquest.models;

public class AdminQuizCategory {
    private String quizCatName;
    private String quizCategDesc;

    public AdminQuizCategory() {
        // Default constructor required for Firebase database operations.
    }

    public AdminQuizCategory(String quizCatName) {
        this.quizCatName = quizCatName;
    }

    public AdminQuizCategory(String categoryName, String categoryDescription) {
        this.quizCatName=categoryName;
        this.quizCategDesc=categoryDescription;
    }

    public String getQuizCatName() {
        return quizCatName;
    }

    public void setQuizCatName(String quizCatName) {
        this.quizCatName = quizCatName;
    }

    public String getQuizCatNm() {
        return quizCatName;
    }

    public void setQuizCatNm(String quizCatNm) {
        this.quizCatName = quizCatNm;
    }
}
