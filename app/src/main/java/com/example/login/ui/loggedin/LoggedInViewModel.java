package com.example.login.ui.loggedin;

import android.content.Context;

import com.example.login.R;
import com.example.login.data.LoginRepository;
import com.example.login.data.Result;
import com.example.login.data.model.LoggedInSession;

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
            this.sessionUpdateResult.setValue(new SessionResult(((Result.Success<LoggedInSession>) result).getData()));
        } else {
            this.sessionUpdateResult.setValue(new SessionResult(R.string.session_get_failed));
        }
    }
}
