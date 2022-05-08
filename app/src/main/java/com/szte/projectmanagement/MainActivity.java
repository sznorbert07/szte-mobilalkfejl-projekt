package com.szte.projectmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_login = findViewById(R.id.button_login);
        Button button_register = findViewById(R.id.button_register);

        button_login.setOnClickListener(this::loginButtonClick);
        button_register.setOnClickListener(this::registerButtonClick);
    }

    private void loginButtonClick(View target) {
        Log.i(LOG_TAG, "Login button pressed");
    }

    private void registerButtonClick(View target) {
        Log.i(LOG_TAG, "Register button pressed");
    }
}