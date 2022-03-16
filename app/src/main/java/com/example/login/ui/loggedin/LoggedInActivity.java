package com.example.login.ui.loggedin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.databinding.ActivityLoggedInBinding;

public class LoggedInActivity extends AppCompatActivity {
    private final static String TAG = LoggedInActivity.class.getSimpleName();

    private ActivityLoggedInBinding binding;
    private LoggedInViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new LoggedInViewModelFactory())
                .get(LoggedInViewModel.class);

        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button loginButton = binding.finishBtn;
        final TextView sessionIdText = binding.sessionId;

        loginButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });

        setSupportActionBar(binding.toolbar);

        viewModel.getSessionResult().observe(this, new Observer<SessionResult>() {
            @Override
            public void onChanged(@Nullable SessionResult sessionResult) {
                if (sessionResult == null) {
                    return;
                }

                if (sessionResult.getError() != null) {
                    if (sessionResult.getError() instanceof Integer) {
                        showSessionGetFailed((Integer) sessionResult.getError());
                    } else if (sessionResult.getError() instanceof String) {
                        showSessionGetFailed((String) sessionResult.getError());
                    }
                }

                if (sessionResult.getSuccess() != null) {
                    String sessionId = sessionResult.getSuccess().getTmdbSession();
                    if (sessionId != null && sessionId.trim().length() > 0) {
                        sessionIdText.setText(sessionId);
                    } else {
                        Log.i(TAG, "session returned null or 0 length string");
                    }
                }
            }
        });

        viewModel.getSession(getApplicationContext());
    }

    private void showSessionGetFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showSessionGetFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}