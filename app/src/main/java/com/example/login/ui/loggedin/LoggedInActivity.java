package com.example.login.ui.loggedin;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.login.R;
import com.example.login.databinding.ActivityLoggedInBinding;

public class LoggedInActivity extends AppCompatActivity {
    private final static String TAG = LoggedInActivity.class.getSimpleName();

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoggedInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button loginButton = binding.finishBtn;
        final TextView sessionIdText = binding.sessionId;

        loginButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

}