package com.codecrafters.quizquest.models;

import java.util.ArrayList;
import java.util.List;

public class QuizCategory {

    private String quizCatName;
    private int quizCatImg;
    private String catKey;
    private int quizzesTaken; // To store the total number of quizzes taken
    private List<QuizHistoryItem> quizHistory; // To store a list of quiz history items

    public QuizCategory(String quizCatName, int quizCatImg, String catKey) {
        this.quizCatName = quizCatName;
        this.quizCatImg = quizCatImg;
        this.catKey = catKey;
        this.quizzesTaken = 0; // Initialize quizzesTaken as 0
        this.quizHistory = new ArrayList<>(); // Initialize an empty list for quiz history
    }

    public String getQuizCatName() {
        return quizCatName;
    }

    public void setQuizCatName(String quizCatName) {
        this.quizCatName = quizCatName;
    }

    public int getQuizCatImg() {
        return quizCatImg;
    }

    public void setQuizCatImg(int quizCatImg) {
        this.quizCatImg = quizCatImg;
    }

    public String getCatKey() {
        return catKey;
    }

    public void setCatKey(String catKey) {
        this.catKey = catKey;
    }

    public int getQuizzesTaken() {
        return quizzesTaken;
    }

    public void setQuizzesTaken(int quizzesTaken) {
        this.quizzesTaken = quizzesTaken;
    }

    public List<QuizHistoryItem> getQuizHistory() {
        return quizHistory;
    }

    public void setQuizHistory(List<QuizHistoryItem> quizHistory) {
        this.quizHistory = quizHistory;
    }
}
