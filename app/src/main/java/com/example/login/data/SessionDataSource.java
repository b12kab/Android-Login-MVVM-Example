package com.example.login.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.login.data.model.LoggedInSession;

public class SessionDataSource {
    private static final String SHARED_PREFERENCE_EDITOR_NAME = "shared_pref_name";
    private static final String SHARED_PREFERENCE_KEY_SESSION_KEY = "session_id";

    public Result<?> getSession(Context context) {
        try {
            String sessionId = context.getSharedPreferences(SHARED_PREFERENCE_EDITOR_NAME, Context.MODE_PRIVATE).
                    getString(SHARED_PREFERENCE_KEY_SESSION_KEY, null);

            LoggedInSession loggedInSession = new LoggedInSession();
            loggedInSession.setTmdbSession(sessionId);

            return new Result.Success<LoggedInSession>(loggedInSession);
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }

    public Result<?> setSession(Context context, String tmdbSession) {
        try {
            final SharedPreferences.Editor editor =
                    context.getSharedPreferences(SHARED_PREFERENCE_EDITOR_NAME, Context.MODE_PRIVATE).edit();
            editor.putString(SHARED_PREFERENCE_KEY_SESSION_KEY, tmdbSession);
            editor.apply();

            return new Result.Success<Boolean>(true);
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }

    public Result<?> deleteSession(Context context) {
        try {
            final SharedPreferences.Editor editor =
                    context.getSharedPreferences(SHARED_PREFERENCE_EDITOR_NAME, Context.MODE_PRIVATE).edit();
            editor.remove(SHARED_PREFERENCE_KEY_SESSION_KEY);
            editor.apply();

            return new Result.Success<Boolean>(true);
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }
}
