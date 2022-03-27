package com.example.login.data;

import android.util.Log;

import com.b12kab.tmdblibrary.Tmdb;
import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.b12kab.tmdblibrary.implementation.SessionHelper;
import com.example.login.app.ThisApplication;
import com.example.login.data.model.LoggedInSessionAndUser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import androidx.annotation.Nullable;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_EXCEPTION_UNKNOWN_CAUSE;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final static String TAG = LoginDataSource.class.getSimpleName();

    SessionHelper sessionHelper = null;
    List<Integer> tmdbSessionCodes = null;

    private LoginDataSource() {}

    public LoginDataSource(SessionHelper sessionHelper) {
        if (sessionHelper == null) {
            throw new NullPointerException("SessionHelper is null in LoginDataSource class creation");
        }
        this.sessionHelper = sessionHelper;
    }

    public Result<?> login(String username, String password) {

        try {
            String session = null;
            for (int i = 0; i < 3; i++) {
                session = this.createSessionInBackground(username, password);
//            if (!StringUtils.isBlank(session)) {
                if (session != null && !session.trim().isEmpty()) {
                    break;
                }
            }

            if (session == null || session.trim().isEmpty()) {
//            if (StringUtils.isBlank(session)) {
                TmdbException e = new TmdbException();
                e.setUseMessage(TmdbException.UseMessage.Yes);
                e.setMessage("Failed to connect to service");
                return new Result.Error(e);
            }

            LoggedInSessionAndUser newSession = new LoggedInSessionAndUser();
            newSession.setTmdbSession(session);
            newSession.setUserId(username);

            return new Result.Success<LoggedInSessionAndUser>(newSession);
        } catch (Exception e) {
            if (e instanceof TmdbException) {
                return new Result.Error(e);
            }

            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<?> logout(String session) {
        return new Result.Success<Boolean>(true);
    }

    private void obtainTmdbSessions() {
        if (tmdbSessionCodes == null || tmdbSessionCodes.size() == 0) {
            tmdbSessionCodes = sessionHelper.getAssocHelperTmdbErrorStatusCodes();
        }
    }

    @Nullable
    private String createSessionInBackground(String username, String password) throws IOException {
        Log.i(TAG, "begin createSessionInBackground");
        Tmdb tmdb = ThisApplication.getTmdb();
        ExecutorService x = ThisApplication.getExecutor();
        Log.i(TAG, "createSessionInBackground - got executor");
        String session = null;
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws IOException {
                return sessionHelper.createTmdbSession(tmdb, username, password);
            }
        };

        try {
            Log.i(TAG, "createSessionInBackground - callable - before submit");
            Future<String> future = x.submit(callable);
            session = future.get(); // awaits the result
            int a = 2;
        } catch (ExecutionException ee) {
            final Throwable throwable;
            if (ee.getCause() != null) {
                throwable = ee.getCause();
                if (throwable instanceof TmdbException) {
                    obtainTmdbSessions();
                    Optional<Integer> item = tmdbSessionCodes.stream().filter(c -> c == ((TmdbException) throwable).getCode()).findFirst();

                    if (item.isPresent()) {
                        ((TmdbException) throwable).setUseMessage(TmdbException.UseMessage.Yes);
                    } else {
                        ((TmdbException) throwable).setUseMessage(TmdbException.UseMessage.No);
                    }

                    Log.i(TAG, "ExecutionException - rethrow TmdbException - exit createSessionInBackground");
                    throw (TmdbException) throwable;
                } else if (throwable instanceof IOException) {
                    Log.i(TAG, "ExecutionException - IOException - exit createSessionInBackground");
                    throw (IOException) throwable;
                }
            } else {
                TmdbException exception = new TmdbException();
                exception.setCode(TMDB_CODE_EXCEPTION_UNKNOWN_CAUSE);
                Log.i(TAG, "ExecutionException - new TmdbException - exit createSessionInBackground");
                throw exception;
            }
        } catch (InterruptedException ie) {
            Log.i(TAG, "createSessionInBackground - InterruptedException exit createSessionInBackground");
            return null;
        }

        Log.i(TAG, "normal exit createSessionInBackground");
        return session;
    }
}