package com.codecrafters.quizquest.models;

public class QuizCategory {

    private String quizCatName;
    private String quizCatImg;
    private String catKey;

    private String quizCatDescription;

    public QuizCategory(String quizCatName, String quizCatImg, String catKey) {
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

    public String getQuizCatImg() {
        return quizCatImg;
    }

    public void setQuizCatImg(String quizCatImg) {
        this.quizCatImg = quizCatImg;
    }

    public String getCatKey() {
        return catKey;
    }

    public void setCatKey(String catKey) {
        this.catKey = catKey;
    }
}
