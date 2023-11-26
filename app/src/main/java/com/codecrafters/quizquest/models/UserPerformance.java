package com.codecrafters.quizquest.models;

public class UserPerformance {
    private String userName;
    private double meanScore;
    private int rank;

    public UserPerformance(String userName, double meanScore) {
        this.userName = userName;
        this.meanScore = meanScore;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getMeanScore() {
        return meanScore;
    }

    public void setMeanScore(double meanScore) {
        this.meanScore = meanScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "UserPerformance{" +
                "userName='" + userName + '\'' +
                ", meanScore=" + meanScore +
                ", rank=" + rank +
                '}';
    }
}
