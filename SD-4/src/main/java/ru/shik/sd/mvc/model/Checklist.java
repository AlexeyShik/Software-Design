package ru.shik.sd.mvc.model;

import java.util.List;

public class Checklist {

    private int id;
    private String name;
    private String description;
    private List<Integer> taskIds;

    public Checklist() {
    }

    public Checklist(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }
}
