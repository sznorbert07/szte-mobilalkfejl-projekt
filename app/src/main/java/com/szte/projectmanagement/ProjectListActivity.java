package com.szte.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.szte.projectmanagement.data.ProjectAdapter;
import com.szte.projectmanagement.data.model.Project;

import java.util.ArrayList;

public class ProjectListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProjectListActivity.class.getName();
    private FirebaseUser user;
    private int gridNumber = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;
    private ArrayList<Project> projectsData;
    private ProjectAdapter projectAdapter;
    private CollectionReference projectsCollection;
    private long itemLimit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        // RecyclerView
        projectsData = new ArrayList<Project>();
        projectAdapter = new ProjectAdapter(this, projectsData);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        recyclerView.setAdapter(projectAdapter);

        // Firestore
        firestore = FirebaseFirestore.getInstance();
        projectsCollection = firestore.collection("Projects");
        queryData();
    }

    private void initializeData() {
        ArrayList<Project> projects = new ArrayList<Project>();
        projects.add(new Project("Personal blog", ""));
        projects.add(new Project("University homeworks", "University related tasks - Semester 3"));
        projects.add(new Project("Home stuffs", "Tasks to do at home"));

        for (Project project : projects) {
            projectsCollection.add(project);
            Log.i(LOG_TAG, "Adding " + project.getName());
        }
    }

    private void queryData() {
        projectsData.clear();
        projectsCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Project project = document.toObject(Project.class);
                        project.setId(document.getId());
                        projectsData.add(project);
                    }

                    Log.i(LOG_TAG, "Size: " + projectsData.size());
                    if (projectsData.size() == 0) {
                        initializeData();
                        queryData();
                    }

                    // Notify the adapter of the change.
                    projectAdapter.notifyDataSetChanged();
                });
    }
}