package com.codecrafters.quizquest.models;

public class QuizQuestionNonAdmin {
    private String QuizQuesID;
    private String QuizSetID;
    private String QuizQuestion;
    private String QuizQuesAnsA;
    private String QuizQuesAnsB;
    private String QuizQuesAnsC;
    private String QuizQuesAnsD;
    private String QuizAnswerKey;
    private String QuizCatID;

    // Constructors (default and parameterized)
    public QuizQuestionNonAdmin() {
        // Default constructor required for Firebase
    }

    public QuizQuestionNonAdmin(String quizQuesID, String quizSetID, String quizQuestion, String quizQuesAnsA, String quizQuesAnsB, String quizQuesAnsC, String quizQuesAnsD, String quizAnswerKey, String quizCatID) {
        QuizQuesID = quizQuesID;
        QuizSetID = quizSetID;
        QuizQuestion = quizQuestion;
        QuizQuesAnsA = quizQuesAnsA;
        QuizQuesAnsB = quizQuesAnsB;
        QuizQuesAnsC = quizQuesAnsC;
        QuizQuesAnsD = quizQuesAnsD;
        QuizAnswerKey = quizAnswerKey;
        QuizCatID = quizCatID;
    }

    // Getters and setters for each attribute

    public String getQuizQuesID() {
        return QuizQuesID;
    }

    public void setQuizQuesID(String quizQuesID) {
        QuizQuesID = quizQuesID;
    }

    public String getQuizSetID() {
        return QuizSetID;
    }

    public void setQuizSetID(String quizSetID) {
        QuizSetID = quizSetID;
    }

    public String getQuizQuestion() {
        return QuizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        QuizQuestion = quizQuestion;
    }

    public String getQuizQuesAnsA() {
        return QuizQuesAnsA;
    }

    public void setQuizQuesAnsA(String quizQuesAnsA) {
        QuizQuesAnsA = quizQuesAnsA;
    }

    public String getQuizQuesAnsB() {
        return QuizQuesAnsB;
    }

    public void setQuizQuesAnsB(String quizQuesAnsB) {
        QuizQuesAnsB = quizQuesAnsB;
    }

    public String getQuizQuesAnsC() {
        return QuizQuesAnsC;
    }

    public void setQuizQuesAnsC(String quizQuesAnsC) {
        QuizQuesAnsC = quizQuesAnsC;
    }

    public String getQuizQuesAnsD() {
        return QuizQuesAnsD;
    }

    public void setQuizQuesAnsD(String quizQuesAnsD) {
        QuizQuesAnsD = quizQuesAnsD;
    }

    public String getQuizAnswerKey() {
        return QuizAnswerKey;
    }

    public void setQuizAnswerKey(String quizAnswerKey) {
        QuizAnswerKey = quizAnswerKey;
    }

    public String getQuizCatID() {
        return QuizCatID;
    }

    public void setQuizCatID(String quizCatID) {
        QuizCatID = quizCatID;
    }
}
