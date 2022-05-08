package com.szte.projectmanagement.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.szte.projectmanagement.R;
import com.szte.projectmanagement.data.model.Project;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private ArrayList<Project> projectsData;

    private Context context;

    public ProjectAdapter(Context context, ArrayList<Project> projectsData) {
        this.context = context;
        this.projectsData = projectsData;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_project, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project currentProject = projectsData.get(position);

        holder.bindTo(currentProject);

        // TODO?? animation
    }

    @Override
    public int getItemCount() {
        return projectsData.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_description;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.textView_name);
            tv_description = itemView.findViewById(R.id.textView_description);
        }

        void bindTo(Project project) {
            tv_name.setText(project.getName());
            tv_description.setText(project.getDescription());
        }
    }
}
