package com.szte.projectmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String PREF_TAG = Objects.requireNonNull(LoginActivity.class.getPackage()).toString();
    private static final String LOG_TAG = LoginActivity.class.getName();

    private Button button_login;
    private Button button_cancelLogin;
    private EditText et_emailAddress;
    private EditText et_password;

    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;

    private Animation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(this::loginButtonClick);

        button_cancelLogin = findViewById(R.id.button_cancelLogin);
        button_cancelLogin.setOnClickListener(this::cancelLoginButtonClick);

        et_emailAddress = findViewById(R.id.editText_login_TextEmailAddress);
        et_password = findViewById(R.id.editText_login_TextPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences(PREF_TAG, MODE_PRIVATE);

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailAddress", et_emailAddress.getText().toString());
        editor.putString("password", et_password.getText().toString());
        editor.apply();

        Log.i(LOG_TAG, "onPuase - Writing shared preferences.");
    }

    private void loginButtonClick(View target) {
        Log.i(LOG_TAG, "Logging in...");

        button_login.startAnimation(scaleAnimation);

        String emailAddress = et_emailAddress.getText().toString();
        String password = et_password.getText().toString();

        if (emailAddress.isEmpty() || password.isEmpty()) {
            String error = "E-mail and password can't be empty.";
            Log.e(LOG_TAG, error);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startProjectActivity();
                // TODO: authstatelistener
            } else {
                String error = "Login error: " + Objects.requireNonNull(task.getException()).getMessage();
                Log.e(LOG_TAG, error);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startProjectActivity() {
        Log.i(LOG_TAG, "Start " + ProjectListActivity.class.getName());

        Intent projectIntent = new Intent(this, ProjectListActivity.class);
        startActivity(projectIntent);
    }

    private void cancelLoginButtonClick(View target) {
        Log.i(LOG_TAG, "Cancel Login.");
        button_cancelLogin.startAnimation(scaleAnimation);
        finish();
    }
}