package com.codecrafters.quizquest.models;

public class AdminQuizSet {
    private String quizCatID;
    private String quizSetId;
    private String quizSetName;
    private long quizSetCreatedOn;
    private String quizSetCreatedBy;
    private long quizSetModifiedOn;
    private String quizSetModifiedBy;

    // Constructors

    public AdminQuizSet() {
        // Default constructor required for Firebase
    }

    // Constructor
    public AdminQuizSet(String quizCatID, String quizSetId, String quizSetName,
                        long quizSetCreatedOn, String quizSetCreatedBy,
                        long quizSetModifiedOn, String quizSetModifiedBy) {
        this.quizCatID = quizCatID;
        this.quizSetId = quizSetId;
        this.quizSetName = quizSetName;
        this.quizSetCreatedOn = quizSetCreatedOn;
        this.quizSetCreatedBy = quizSetCreatedBy;
        this.quizSetModifiedOn = quizSetModifiedOn;
        this.quizSetModifiedBy = quizSetModifiedBy;
    }

    // Getter and Setter methods
    public String getQuizCatID() {
        return quizCatID;
    }

    public void setQuizCatID(String quizCatID) {
        this.quizCatID = quizCatID;
    }

    public String getQuizSetId() {
        return quizSetId;
    }

    public void setQuizSetId(String quizSetId) {
        this.quizSetId = quizSetId;
    }

    public String getQuizSetName() {
        return quizSetName;
    }

    public void setQuizSetName(String quizSetName) {
        this.quizSetName = quizSetName;
    }

    public long getQuizSetCreatedOn() {
        return quizSetCreatedOn;
    }

    public void setQuizSetCreatedOn(long quizSetCreatedOn) {
        this.quizSetCreatedOn = quizSetCreatedOn;
    }

    public String getQuizSetCreatedBy() {
        return quizSetCreatedBy;
    }

    public void setQuizSetCreatedBy(String quizSetCreatedBy) {
        this.quizSetCreatedBy = quizSetCreatedBy;
    }

    public long getQuizSetModifiedOn() {
        return quizSetModifiedOn;
    }

    public void setQuizSetModifiedOn(long quizSetModifiedOn) {
        this.quizSetModifiedOn = quizSetModifiedOn;
    }

    public String getQuizSetModifiedBy() {
        return quizSetModifiedBy;
    }

    public void setQuizSetModifiedBy(String quizSetModifiedBy) {
        this.quizSetModifiedBy = quizSetModifiedBy;
    }
}
