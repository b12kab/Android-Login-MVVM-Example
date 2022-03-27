package com.example.login.data;

import android.content.Context;

public interface ISessionDataSource {
    static final String KEYNAME_SESSION_KEY = "session_id";

    Result<?> getSession(Context context);
    Result<?> setSession(Context context, String tmdbSession);
    Result<?> deleteSession(Context context);
}
