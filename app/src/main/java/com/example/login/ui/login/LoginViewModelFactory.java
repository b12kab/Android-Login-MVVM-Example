package com.example.login.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.b12kab.tmdblibrary.implementation.SessionHelper;
import com.example.login.data.ISessionDataSource;
import com.example.login.data.LoginDataSource;
import com.example.login.data.LoginRepository;
import com.example.login.data.SessionDataStoreDataSource;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            // TODO: add injection later
            SessionHelper sessionHelper = new SessionHelper();
//            ISessionDataSource dataSource = new SessionPreferenceDataSource();
            ISessionDataSource dataSource = new SessionDataStoreDataSource();
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource(sessionHelper), dataSource));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}