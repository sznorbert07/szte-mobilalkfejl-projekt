package com.szte.projectmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    private FloatingActionButton fab_add;

    private ArrayList<Project> projectsData;
    private ProjectAdapter projectAdapter;
    private CollectionReference projectsCollection;
    private long itemLimit = 10;
    private Animation scaleAnimation;

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(projectAdapter);

        // Firestore
        firestore = FirebaseFirestore.getInstance();
        projectsCollection = firestore.collection("Projects");
        queryData();

        // Add button
        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(view -> {
            fab_add.startAnimation(scaleAnimation);
            Intent createProjectIntent = new Intent(this, CreateProjectActivity.class);
            startActivity(createProjectIntent);
        });

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
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
        projectsCollection.orderBy("name", Query.Direction.ASCENDING).get()
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

    public void deleteProject(Project project) {
        DocumentReference ref = projectsCollection.document(project._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Project is successfully deleted: " + project._getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Project " + project._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });

        queryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.project_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                projectAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}