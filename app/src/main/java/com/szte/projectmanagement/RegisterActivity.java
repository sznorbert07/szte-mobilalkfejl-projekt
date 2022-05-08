package com.szte.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();

    private EditText et_username;
    private EditText et_emailAddress;
    private EditText et_password;
    private EditText et_passwordAgain;
    private Button button_register;
    private Button button_cancelLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button_register = findViewById(R.id.button_register);
        button_register.setOnClickListener(this::registerButtonClick);

        button_cancelLogin = findViewById(R.id.button_cancelRegister);
        button_cancelLogin.setOnClickListener(this::cancelRegisterButtonClick);

        et_username = findViewById(R.id.editText_register_TextUsername);
        et_emailAddress = findViewById(R.id.editText_register_TextEmailAddress);
        et_password = findViewById(R.id.editText_register_TextPassword);
        et_passwordAgain = findViewById(R.id.editText_register_TextPasswordAgain);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerButtonClick(View view) {
        Log.i(LOG_TAG, "Registrating...");

        String username = et_username.getText().toString();
        String emailAddress = et_emailAddress.getText().toString();
        String password = et_password.getText().toString();
        String passwordAgain = et_passwordAgain.getText().toString();

        if (username.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            makeErrorMessage("Every field must be filled.");
            return;
        }

        if (!password.equals(passwordAgain)) {
            makeErrorMessage("Passwords are not equal.");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.i(LOG_TAG, "User registered successfully");
                startProjectActivity();
            } else {
                makeErrorMessage("Error during registration: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void startProjectActivity() {
        Log.i(LOG_TAG, "Start " + ProjectListActivity.class.getName());

        Intent projectIntent = new Intent(this, ProjectListActivity.class);
        startActivity(projectIntent);
    }

    private void makeErrorMessage(String errorMessage) {
        Log.e(LOG_TAG, errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void cancelRegisterButtonClick(View view) {
        Log.i(LOG_TAG, "Cancel Registration.");
        finish();
    }
}