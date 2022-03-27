package com.example.login.data;

import android.content.Context;
import android.util.Log;

import com.example.login.data.model.LoggedInSession;

/***
 * This uses the genericified Preference implementation. Works :)
 */
public class SessionDataStoreDataSource implements ISessionDataSource {
    private final static String TAG = SessionDataStoreDataSource.class.getSimpleName();

    private DataStorePreferenceGenericified dataStorePreference = null;

    @Override
    public Result<?> getSession(Context context) {
        try {
            Log.i(TAG, "getSession - start");
            this.init(context);
            Log.i(TAG, "getSession - init complete");

            String session = dataStorePreference.get(KEYNAME_SESSION_KEY);
            String sRes;
            if (session == null) sRes = "null"; else sRes = session;
            Log.i(TAG, "getSession - getString result: " + sRes);

            LoggedInSession loggedInSession = new LoggedInSession();
            loggedInSession.setTmdbSession(session);

            Log.i(TAG, "getSession - complete - before return ");
            return new Result.Success<LoggedInSession>(loggedInSession);
        } catch (Exception e) {
            Log.i(TAG, "getSession - exception - " + e.toString());
            return new Result.Error(e);
        }
    }

    @Override
    public Result<?> setSession(Context context, String tmdbSession) {
        try {
            Log.i(TAG, "setSession - start");
            this.init(context);

            dataStorePreference.put(KEYNAME_SESSION_KEY, tmdbSession);

            Log.i(TAG, "setSession - complete");
            return new Result.Success<Boolean>(true);
        } catch (Exception e) {
            Log.i(TAG, "setSession - exception - " + e.toString());
            return new Result.Error(e);
        }
    }

    @Override
    public Result<?> deleteSession(Context context) {
        Log.i(TAG, "deleteSession - start");
        this.init(context);

        return new Result.Error(new Exception("delete not implemented"));
    }

    /***
     *
     * @param context
     */
    private void init(Context context) {
        Log.i(TAG, "init - start");
        if (dataStorePreference == null) {
            dataStorePreference = new DataStorePreferenceGenericified();
        }

        if (!dataStorePreference.isDataStoreInitialized()) {
            Log.i(TAG, "init - dataStore - init");
            dataStorePreference.init(context);
        }
        Log.i(TAG, "init - complete");
    }
}
