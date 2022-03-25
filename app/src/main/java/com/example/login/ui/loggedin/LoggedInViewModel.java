package com.example.login.ui.loggedin;

import android.content.Context;

import com.example.login.R;
import com.example.login.data.LoginRepository;
import com.example.login.data.Result;
import com.example.login.data.model.LoggedInSession;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoggedInViewModel extends ViewModel {
    private MutableLiveData<SessionResult> sessionUpdateResult = new MutableLiveData<>();

    private LoginRepository loginRepository;

    LoggedInViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<SessionResult> getSessionResult() {
        return this.sessionUpdateResult;
    }

    public void getSession(Context context) {
        Result<?> result = loginRepository.getSession(context);
        if (result instanceof Result.Success) {
            // for security only show a portion of the result
            String fullSession = ((Result.Success<LoggedInSession>) result).getData().getTmdbSession();
            String sessionSubset = getSessionSubset(fullSession);
            LoggedInSession session = new LoggedInSession();
            session.setTmdbSession(sessionSubset);
            this.sessionUpdateResult.setValue(new SessionResult(session));
        } else {
            this.sessionUpdateResult.setValue(new SessionResult(R.string.session_get_failed));
        }
    }

    @NonNull
    private String getSessionSubset(String session) {
        String showSess = session;
        if (session == null) {
            return "<Empty>";
        }

        int len = session.length();
        if (len > 0) {
            int beginLen = new Random().nextInt(len);
            int endLen = len -1;
            if (len - beginLen > 10) {
                endLen = beginLen + 10;
            }
            showSess = session.substring(beginLen, endLen);
        } else {
            showSess = "<Empty>";
        }

        return showSess;
    }
}
