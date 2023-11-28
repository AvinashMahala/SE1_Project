package com.codecrafters.quizquest.models;

public class AdminUser  {
    private String UserID;
    private String UserPhNo;
    private String UserFname;
    private String UserEmail;
    private String UserDOB;
    private String UserRole;
    private long UserCreatedOn;

    private String UserStatus;

    // Default constructor required for Firebase
    public AdminUser() {
        // Default constructor is required for Firebase data mapping
    }

    // Constructor with all fields
    public AdminUser(String UserID, String UserPhNo, String UserFname, String UserEmail,
                     String UserDOB, String UserRole, long UserCreatedOn , String UserStatus) {
        this.UserID = UserID;
        this.UserPhNo = UserPhNo;
        this.UserFname = UserFname;
        this.UserEmail = UserEmail;
        this.UserDOB = UserDOB;
        this.UserRole = UserRole;
        this.UserCreatedOn = UserCreatedOn;
        this.UserStatus= UserStatus;

    }

    // Getters and setters
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    public String getUserPhNo() {
        return UserPhNo;
    }

    public void setUserPhNo(String userPhNo) {
        this.UserPhNo = userPhNo;
    }

    public String getUserFname() {
        return UserFname;
    }

    public void setUserFname(String userFname) {
        this.UserFname = userFname;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        this.UserEmail = userEmail;
    }

    public String getUserDOB() {
        return UserDOB;
    }

    public void setUserDOB(String userDOB) {
        this.UserDOB = userDOB;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        this.UserRole = userRole;
    }

    public long getUserCreatedOn() {
        return UserCreatedOn;
    }

    public void setUserCreatedOn(long userCreatedOn) {
        this.UserCreatedOn = userCreatedOn;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String UserStatus) {
        this.UserStatus = UserStatus;
    }



}
