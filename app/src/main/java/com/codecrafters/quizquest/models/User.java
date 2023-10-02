package com.codecrafters.quizquest.models;

public class User {
    private String name;
    private int quizzesTaken;

    // Constructor, getters, and setters

    public User(String name, int quizzesTaken) {
        this.name = name;
        this.quizzesTaken = quizzesTaken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuizzesTaken() {
        return quizzesTaken;
    }

    public void setQuizzesTaken(int quizzesTaken) {
        this.quizzesTaken = quizzesTaken;
    }
}
