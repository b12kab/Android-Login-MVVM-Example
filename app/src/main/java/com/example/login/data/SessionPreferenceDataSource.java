package com.example.login.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.example.login.app.ThisApplication;
import com.example.login.data.model.LoggedInSession;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.b12kab.tmdblibrary.NetworkHelper.TmdbCodes.TMDB_CODE_EXCEPTION_UNKNOWN_CAUSE;
import static java.lang.Thread.sleep;

public class SessionPreferenceDataSource implements ISessionDataSource {
    private final static String TAG = SessionPreferenceDataSource.class.getSimpleName();

    private static final String SHARED_PREFERENCE_EDITOR_NAME = "shared_pref_name";
    private static final String SHARED_PREFERENCE_KEY_SESSION_KEY = "session_id";

    public Result<?> getSession(Context context) {
        try {
            String sessionId = context.getSharedPreferences(SHARED_PREFERENCE_EDITOR_NAME, Context.MODE_PRIVATE).
                    getString(SHARED_PREFERENCE_KEY_SESSION_KEY, null);

            /*
             * Under conditions where there is more data in the shared preference, this may cause ANR's.
             * Given that there is little data, this is highly unlikely.
             */
            LoggedInSession loggedInSession = new LoggedInSession();
            loggedInSession.setTmdbSession(sessionId);

            return new Result.Success<LoggedInSession>(loggedInSession);
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }

    public Result<?> setSession(Context context, String tmdbSession) {
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "setResult - runnable - start");
                    final SharedPreferences.Editor editor =
                            context.getSharedPreferences(SHARED_PREFERENCE_EDITOR_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString(SHARED_PREFERENCE_KEY_SESSION_KEY, tmdbSession);
                    editor.apply();
                    Log.i(TAG, "setResult - runnable - finish");
                }
            };

            // Note: apply's under certain conditions can cause an ANR on the UI thread, so run it on a different thread
            this.setResult(runnable);

            return new Result.Success<Boolean>(true);
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }

    public Result<?> deleteSession(Context context) {
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "deleteSession - runnable - start");
                    final SharedPreferences.Editor editor =
                            context.getSharedPreferences(SHARED_PREFERENCE_EDITOR_NAME, Context.MODE_PRIVATE).edit();
                    editor.remove(SHARED_PREFERENCE_KEY_SESSION_KEY);
                    editor.apply();
                    Log.i(TAG, "deleteSession - runnable - finish");
                }
            };

            // Note: apply's under certain conditions can cause an ANR on the UI thread, so run it on a different thread
            this.setResult(runnable);

            return new Result.Success<Boolean>(true);
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }

    private void setResult(Runnable runnable) throws Exception {
        ExecutorService x = ThisApplication.getExecutor();

        try {
            Log.i(TAG, "setResult - about to submit runnable");
            Future<?> future = x.submit(runnable);
            future.get();
            while(!future.isDone()) {
                sleep(500);
            }
            Log.i(TAG, "setResult - runnable complete");
        } catch (ExecutionException ee) {
            if (ee.getCause() != null) {
                if (ee.getCause() instanceof Exception )
                    throw (Exception) ee.getCause();
            }

            TmdbException exception = new TmdbException();
            exception.setCode(TMDB_CODE_EXCEPTION_UNKNOWN_CAUSE);
            Log.i(TAG, "setResult - ExecutionException non Exception cause - so throw new TmdbException");
            throw exception;
        } catch (InterruptedException ie) {
            Log.i(TAG, "setResult - InterruptedException");
            int a = 5;
        }

        Log.i(TAG, "setResult - normal exit");
    }
}
