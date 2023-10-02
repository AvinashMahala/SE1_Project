package com.codecrafters.quizquest.models;

public class QuizHistoryItem {
    private String quizId;
    private int score;
    private String date; // You can use a suitable date format

    public QuizHistoryItem() {
        // Default constructor required for Firebase
    }

    public QuizHistoryItem(String quizId, int score, String date) {
        this.quizId = quizId;
        this.score = score;
        this.date = date;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
