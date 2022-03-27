package com.example.login.ui.loggedin;

import com.b12kab.tmdblibrary.implementation.SessionHelper;
import com.example.login.data.ISessionDataSource;
import com.example.login.data.LoginDataSource;
import com.example.login.data.LoginRepository;
import com.example.login.data.SessionDataStoreDataSource;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LoggedInViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(LoggedInViewModel.class)) {
            // TODO: add injection later
            SessionHelper sessionHelper = new SessionHelper();
//            ISessionDataSource dataSource = new SessionPreferenceDataSource();
            ISessionDataSource dataSource = new SessionDataStoreDataSource();
            return (T) new LoggedInViewModel(LoginRepository.getInstance(new LoginDataSource(sessionHelper), dataSource));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
