package com.codecrafters.quizquest.models;

public class UserPerformance {
    private String userName;
    private int score;
    private int rank;

    // Default constructor
    public UserPerformance() {
    }

    // Parameterized constructor
    public UserPerformance(String userName, int score, int rank) {
        this.userName = userName;
        this.score = score;
        this.rank = rank;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    // You might also want to override toString() for easy logging/debugging
    @Override
    public String toString() {
        return "UserPerformance{" +
                "userName='" + userName + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }
}
