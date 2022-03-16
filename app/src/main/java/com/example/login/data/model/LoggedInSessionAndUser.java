package com.example.login.data.model;

public class LoggedInSessionAndUser extends LoggedInSession {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return getUserId();
    }
}
