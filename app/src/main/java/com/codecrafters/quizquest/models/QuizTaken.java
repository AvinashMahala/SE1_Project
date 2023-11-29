package com.codecrafters.quizquest.models;

public class QuizTaken {

    private String QuizID;
    private String UserID;
    private String QuizCatID;
    private int QuizTakenScore;
    private String QuizSetId;
    private String QuizTakenOn;
    private String UserModifiedBy;

    public QuizTaken() {
        // Default constructor required for certain frameworks (e.g., Firebase)
    }

    public QuizTaken(String quizId, String userId, String quizCatId, int quizTakenScore, String quizSetId, String quizTakenOn, String userModifiedBy) {
        this.QuizID = quizId;
        this.UserID = userId;
        this.QuizCatID = quizCatId;
        this.QuizTakenScore = quizTakenScore;
        this.QuizSetId = quizSetId;
        this.QuizTakenOn = quizTakenOn;
        this.UserModifiedBy = userModifiedBy;
    }

    public String getQuizId() {
        return QuizID;
    }

    public void setQuizId(String quizId) {
        this.QuizID = quizId;
    }

    public String getUserId() {
        return UserID;
    }

    public void setUserId(String userId) {
        this.UserID = userId;
    }

    public String getQuizCatId() {
        return QuizCatID;
    }

    public void setQuizCatId(String quizCatId) {
        this.QuizCatID = quizCatId;
    }

    public int getQuizTakenScore() {
        return QuizTakenScore;
    }

    public void setQuizTakenScore(int quizTakenScore) {
        this.QuizTakenScore = quizTakenScore;
    }

    public String getQuizSetId() {
        return QuizSetId;
    }

    public void setQuizSetId(String quizSetId) {
        this.QuizSetId = quizSetId;
    }

    public String getQuizTakenOn() {
        return QuizTakenOn;
    }

    public void setQuizTakenOn(String quizTakenOn) {
        this.QuizTakenOn = quizTakenOn;
    }

    public String getUserModifiedBy() {
        return UserModifiedBy;
    }

    public void setUserModifiedBy(String userModifiedBy) {
        this.UserModifiedBy = userModifiedBy;
    }
}