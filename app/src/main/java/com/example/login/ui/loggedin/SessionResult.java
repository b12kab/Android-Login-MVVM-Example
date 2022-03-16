package com.example.login.ui.loggedin;

import com.example.login.data.model.LoggedInSession;

import androidx.annotation.Nullable;

public class SessionResult {
    @Nullable
    private LoggedInSession success;
    @Nullable
    private Object error;

    SessionResult(@Nullable Object error) {
        this.error = error;
    }

    SessionResult(@Nullable LoggedInSession success) {
        this.success = success;
    }

    @Nullable
    public LoggedInSession getSuccess() {
        return success;
    }

    @Nullable
    public Object getError() {
        return error;
    }
}
