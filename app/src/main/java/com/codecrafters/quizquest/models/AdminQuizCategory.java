package com.codecrafters.quizquest.models;

public class AdminQuizCategory {
    private String quizCatID;
    private String quizCatNm;
    private String quizCatDesc;
    private String quizCateImgUrl;
    private long quizCatCreatedOn;
    private String quizCatCreatedBy;
    private long quizCatModifiedOn;
    private String quizCatModifiedBy;

    // Constructors

    public AdminQuizCategory() {
        // Default constructor required for Firebase
    }

    public AdminQuizCategory(String quizCatID, String quizCatNm, String quizCatDesc, String quizCateImgUrl,
                             long quizCatCreatedOn, String quizCatCreatedBy, long quizCatModifiedOn,
                             String quizCatModifiedBy) {
        this.quizCatID = quizCatID;
        this.quizCatNm = quizCatNm;
        this.quizCatDesc = quizCatDesc;
        this.quizCateImgUrl = quizCateImgUrl;
        this.quizCatCreatedOn = quizCatCreatedOn;
        this.quizCatCreatedBy = quizCatCreatedBy;
        this.quizCatModifiedOn = quizCatModifiedOn;
        this.quizCatModifiedBy = quizCatModifiedBy;
    }

    // Getters and Setters

    public String getQuizCatID() {
        return quizCatID;
    }

    public void setQuizCatID(String quizCatID) {
        this.quizCatID = quizCatID;
    }

    public String getQuizCatNm() {
        return quizCatNm;
    }

    public void setQuizCatNm(String quizCatNm) {
        this.quizCatNm = quizCatNm;
    }

    public String getQuizCatDesc() {
        return quizCatDesc;
    }

    public void setQuizCatDesc(String quizCatDesc) {
        this.quizCatDesc = quizCatDesc;
    }

    public String getQuizCateImgUrl() {
        return quizCateImgUrl;
    }

    public void setQuizCateImgUrl(String quizCateImgUrl) {
        this.quizCateImgUrl = quizCateImgUrl;
    }

    public long getQuizCatCreatedOn() {
        return quizCatCreatedOn;
    }

    public void setQuizCatCreatedOn(long quizCatCreatedOn) {
        this.quizCatCreatedOn = quizCatCreatedOn;
    }

    public String getQuizCatCreatedBy() {
        return quizCatCreatedBy;
    }

    public void setQuizCatCreatedBy(String quizCatCreatedBy) {
        this.quizCatCreatedBy = quizCatCreatedBy;
    }

    public long getQuizCatModifiedOn() {
        return quizCatModifiedOn;
    }

    public void setQuizCatModifiedOn(long quizCatModifiedOn) {
        this.quizCatModifiedOn = quizCatModifiedOn;
    }

    public String getQuizCatModifiedBy() {
        return quizCatModifiedBy;
    }

    public void setQuizCatModifiedBy(String quizCatModifiedBy) {
        this.quizCatModifiedBy = quizCatModifiedBy;
    }
}
