package com.szte.projectmanagement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.szte.projectmanagement.data.model.Project;

import java.util.Objects;

public class CreateProjectActivity extends AppCompatActivity {
    private static final String LOG_TAG = CreateProjectActivity.class.getName();

    private Button button_createProject;
    private EditText et_name;
    private EditText et_description;

    private FirebaseFirestore firestore;
    private NotificationHelper notificationHelper;
    private CollectionReference projectsCollection;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        button_createProject = findViewById(R.id.button_createProject);
        et_name = findViewById(R.id.editText_createProject_TextName);
        et_description = findViewById(R.id.editText_createProject_TextMultilineDescription);

        button_createProject.setOnClickListener(this::onCreateProjectButtonClicked);

        firestore = FirebaseFirestore.getInstance();
        projectsCollection = firestore.collection("Projects");
        notificationHelper = new NotificationHelper(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onCreateProjectButtonClicked(View view) {
        String name = et_name.getText().toString();
        String description = et_description.getText().toString();

        if (name.isEmpty() || description.isEmpty()) {
            makeErrorMessage("Name and description can't be empty.");
            return;
        }

        Project project = new Project(name, description);
        projectsCollection.add(project).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                makeErrorMessage("Error: " + Objects.requireNonNull(task.getException()).getMessage());
            } else {
                Toast.makeText(this, "Project created!", Toast.LENGTH_LONG).show();
                notificationHelper.send("New project created!");
                finish();
            }
        });
    }

    private void makeErrorMessage(String errorMessage) {
        Log.e(LOG_TAG, errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}