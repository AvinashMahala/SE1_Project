package com.codecrafters.quizquest.models;

import android.util.Log;

import com.google.firebase.database.PropertyName;
import java.util.HashMap;
import java.util.Map;

public class QuizQuestion {
    @PropertyName("QuizQuestion")
    private String questionText;
    private String questionId;

    private Map<String, String> options = new HashMap<>();

    @PropertyName("QuizAnswerKey")
    private String correctAnswer;


    public QuizQuestion() {
        // Default constructor required for Firebase
    }


    @PropertyName("QuizQuestion")
    public String getQuestionText() {
        return questionText;
    }

    @PropertyName("QuizQuestion")
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @PropertyName("QuizQuesID")
    public String getQuestionId() {
        Log.d("QuizQuestion", "Received quesID:  " + questionId);
        return questionId;
    }

    @PropertyName("QuizQuesID")
    public void setQuestionId(String questionId) {
        Log.d("QuizQuestion", "Set quesID:  " + questionId);
        this.questionId = questionId;
    }

    public Map<String, String> getOptions() {
        if (options.isEmpty()) {
            // Populate options from individual answer fields
            options.put("A", QuizQuesAnsA);
            options.put("B", QuizQuesAnsB);
            options.put("C", QuizQuesAnsC);
            options.put("D", QuizQuesAnsD);
        }
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    @PropertyName("QuizAnswerKey")
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @PropertyName("QuizAnswerKey")
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getFormattedOptions() {
        StringBuilder formattedOptions = new StringBuilder();
        for (Map.Entry<String, String> entry : getOptions().entrySet()) {
            formattedOptions.append(entry.getKey())
                    .append(") ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return formattedOptions.toString();
    }

    private String QuizQuesAnsA;
    private String QuizQuesAnsB;
    private String QuizQuesAnsC;
    private String QuizQuesAnsD;

    // Getters and setters for individual answer fields

    @PropertyName("QuizQuesAnsA")
    public String getQuizQuesAnsA() {
        return QuizQuesAnsA;
    }

    @PropertyName("QuizQuesAnsA")
    public void setQuizQuesAnsA(String quizQuesAnsA) {
        QuizQuesAnsA = quizQuesAnsA;
    }

    @PropertyName("QuizQuesAnsB")
    public String getQuizQuesAnsB() {
        return QuizQuesAnsB;
    }

    @PropertyName("QuizQuesAnsB")
    public void setQuizQuesAnsB(String quizQuesAnsB) {
        QuizQuesAnsB = quizQuesAnsB;
    }

    @PropertyName("QuizQuesAnsC")
    public String getQuizQuesAnsC() {
        return QuizQuesAnsC;
    }

    @PropertyName("QuizQuesAnsC")
    public void setQuizQuesAnsC(String quizQuesAnsC) {
        QuizQuesAnsC = quizQuesAnsC;
    }

    @PropertyName("QuizQuesAnsD")
    public String getQuizQuesAnsD() {
        return QuizQuesAnsD;
    }

    @PropertyName("QuizQuesAnsD")
    public void setQuizQuesAnsD(String quizQuesAnsD) {
        QuizQuesAnsD = quizQuesAnsD;
    }


}
