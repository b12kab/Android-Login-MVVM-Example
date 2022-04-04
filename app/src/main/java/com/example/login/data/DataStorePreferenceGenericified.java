package com.example.login.data;

import android.content.Context;
import android.util.Log;

import java.util.NoSuchElementException;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

/***
 * This is a genericified version of the working blog article
 * https://programmersought.com/article/997810519714/
 */
public class DataStorePreferenceGenericified {
    private final static String TAG = DataStorePreferenceGenericified.class.getSimpleName();
    public static final String DATASOURCE_STORE_NAME = "user_prefs_session";

    private static RxDataStore<Preferences> dataStore = null;

    /***
     * Checks to see if DataStore has been initialized.
     * @return true = yes, false = no
     */
    public boolean isDataStoreInitialized() {
        return dataStore != null;
    }

    /***
     * Should call before 1st use
     * @param context Context
     */
    public void init(Context context) {
        if (dataStore == null) {
            Log.i(TAG, "init - build DataStore");
            dataStore = new RxPreferenceDataStoreBuilder(context, /*name=*/ DATASOURCE_STORE_NAME).build();
        }
    }

    /***
     * Put item into preference
     * @param keyName key name
     * @param value update value
     */
    public <T> void put(String keyName, T value) {
        Log.i(TAG, "put - start");
        Preferences.Key<T> key = new Preferences.Key<>(keyName);

        Log.i(TAG, "put - key: " + key.toString());
        dataStore.updateDataAsync(preferences -> {
            Log.i(TAG, "put - updateDataAsync - start");
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(key, value);
            Log.i(TAG, "put - updateDataAsync - return Single");
            return Single.just(mutablePreferences);
        });
        Log.i(TAG, "put - completed");
    }

    /***
     * Get item from preference
     * @param keyName key name
     * @return result
     */
    public <T> T get(String keyName) {
        Log.i(TAG, "get - start");
        Preferences.Key<T> key = new Preferences.Key<>(keyName);

        Log.i(TAG, "get - start - key: " + key.toString());
        Flowable<T> flowableResult = dataStore.data().map(preferences -> {
            Log.i(TAG, "get - apply: " + preferences.get(key));
            return preferences.get(key);
        });
        Log.i(TAG, "get - Flowable created");

        T result = null;
        try {
            result = flowableResult.blockingFirst();
        } catch (NoSuchElementException nse) {
            Log.i(TAG, "get - Flowable result - NoSuchElementException");
            // if nothing found, this could be thrown - keep result = null
        } catch (NullPointerException npe) {
            Log.i(TAG, "get - Flowable result - NullPointerException - missing data");
            // if nothing found, this could be thrown - keep result = null
        }

        /* convert the result to print out on log */
        String sRes;
        if (result == null) sRes = "null"; else sRes = result.toString();

        Log.i(TAG, "get - end - result: " + sRes);

        return result;
    }
}
