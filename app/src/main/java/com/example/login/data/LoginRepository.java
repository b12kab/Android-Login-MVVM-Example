package com.example.login.data;

import android.content.Context;

import com.example.login.data.model.LoggedInSessionAndUser;


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource loginDataSource;
    private SessionDataSource sessionDataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
//    private LoggedInUser user = null;
    private LoggedInSessionAndUser session = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource loginDataSource, SessionDataSource sessionDataSource) {
        this.loginDataSource = loginDataSource;
        this.sessionDataSource = sessionDataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource, SessionDataSource sessionDataSource) {
        if(instance == null){
            instance = new LoginRepository(dataSource, sessionDataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return session != null;
    }

    public void logout() {
        loginDataSource.logout(session.getTmdbSession());
        session = null;
    }

    private void setLoggedInUser(LoggedInSessionAndUser session) {
        this.session = session;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<?> login(String username, String password) {
        Result<?> result = loginDataSource.login(username, password);
        if (result instanceof Result.Success) {
            this.setLoggedInUser(((Result.Success<LoggedInSessionAndUser>) result).getData());
        }
        return result;
    }

    public Result<?> setSession(Context context, String session) {
        return sessionDataSource.setSession(context, session);
    }

    public Result<?> getSession(Context context) {
        return sessionDataSource.getSession(context);
    }
}