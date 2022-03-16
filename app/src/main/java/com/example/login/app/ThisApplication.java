package com.example.login.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.b12kab.tmdblibrary.Tmdb;
import com.example.login.BuildConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThisApplication extends Application {
    private static Tmdb tmdb = null;
    private static ExecutorService executor = Executors.newScheduledThreadPool(5);

    /**
     * This is a subclass of {@link Application} used to provide shared objects for this app
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Normal app init code...
        Log.i("Application", "before getExecutor");
        getExecutor(); // warm up threads
        Log.i("Application", "after getExecutor");
    }

    public static synchronized Tmdb getTmdb() {
        if (tmdb == null) {
            tmdb = new Tmdb();
            tmdb.setApiKey(BuildConfig.tmdbApiKey);
        }
        return tmdb;
    }

    public static synchronized ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(5);
        }
        return executor;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
