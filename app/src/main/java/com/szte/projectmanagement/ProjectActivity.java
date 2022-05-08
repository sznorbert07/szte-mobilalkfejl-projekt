package com.szte.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProjectActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        TextView textView = findViewById(R.id.textView);

        firebaseAuth = FirebaseAuth.getInstance();

        textView.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
    }
}