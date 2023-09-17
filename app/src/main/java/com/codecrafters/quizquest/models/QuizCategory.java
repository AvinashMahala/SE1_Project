package com.codecrafters.quizquest.models;

public class QuizCategory {

    private String quizCatName;
    private int quizCatImg;
    private String catKey;

    public QuizCategory(String quizCatName, int quizCatImg, String catKey) {
        this.quizCatName = quizCatName;
        this.quizCatImg = quizCatImg;
        this.catKey = catKey;
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
}
