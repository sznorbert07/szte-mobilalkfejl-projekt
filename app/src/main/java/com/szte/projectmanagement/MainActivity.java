package com.szte.projectmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private Button button_openLogin;
    private Button button_openRegister;
    private Animation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_openLogin = findViewById(R.id.button_openLogin);
        button_openRegister = findViewById(R.id.button_openRegister);

        button_openLogin.setOnClickListener(this::openLoginButtonClick);
        button_openRegister.setOnClickListener(this::openRegisterButtonClick);

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
    }

    private void openLoginButtonClick(View target) {
        Log.i(LOG_TAG, "Opening " + LoginActivity.class.getName());

        button_openLogin.startAnimation(scaleAnimation);

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void openRegisterButtonClick(View target) {
        Log.i(LOG_TAG, "Opening " + RegisterActivity.class.getName());

        button_openRegister.startAnimation(scaleAnimation);

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}