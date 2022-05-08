package com.szte.projectmanagement.data.model;

public class Project {
    private String id;
    private String name;
    private String description;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Project() {
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
