package com.example.login.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.b12kab.tmdblibrary.exceptions.TmdbException;
import com.example.login.data.LoginRepository;
import com.example.login.data.Result;
import com.example.login.data.model.LoggedInSessionAndUser;
import com.example.login.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginResult> sessionUpdateResult = new MutableLiveData<>();

    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return this.loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return this.loginResult;
    }

    LiveData<LoginResult> getSessionUpdateResult() {
        return this.sessionUpdateResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInSessionAndUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInSessionAndUser data = ((Result.Success<LoggedInSessionAndUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), data.getTmdbSession())));
        } else {
            boolean useStringsXmlErrorMsg = true;
            if (result instanceof Result.Error) {
                Exception ex = ((Result.Error) result).getError();
                if (ex instanceof TmdbException) {
                    TmdbException tmdbException = (TmdbException) ex;
                    if (tmdbException.getUseMessage() == TmdbException.UseMessage.Yes && ex.getMessage() != null && !ex.getMessage().trim().isEmpty() ) {
                        loginResult.setValue(new LoginResult(tmdbException.getMessage()));
                        useStringsXmlErrorMsg = false;
                    }
                }
            }

            if (useStringsXmlErrorMsg) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }
    }

    public void updateSession(Context context, String tmdbSessionId) {
        Result<?> result = loginRepository.setSession(context, tmdbSessionId);
        if (result instanceof Result.Success) {
            this.sessionUpdateResult.setValue(new LoginResult(LoggedInUserView.createSessionIdOnly(tmdbSessionId)));
        } else {
            this.sessionUpdateResult.setValue(new LoginResult(new LoginResult(R.string.session_save_failed)));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            this.loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            this.loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            this.loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}