package com.example.login.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String sessionId;

    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    LoggedInUserView(String displayName, String sessionId) {
        this.displayName = displayName;
        this.sessionId = sessionId;
    }

    public static LoggedInUserView createSessionIdOnly(String sessionId) {
        LoggedInUserView userView = new LoggedInUserView(null);
        userView.sessionId = sessionId;
        return userView;
    }

    String getDisplayName() {
        return displayName;
    }

    String getSessionId() {
        return this.sessionId;
    }
}