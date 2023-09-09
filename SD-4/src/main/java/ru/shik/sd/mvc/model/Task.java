package ru.shik.sd.mvc.model;

public class Task {

    private int id;
    private String name;
    private String description;
    private short complexity;
    private short significance;
    private boolean completed;

    public Task() {

    }

    public Task(int id, String name, String description, short complexity, short significance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.complexity = complexity;
        this.significance = significance;
    }

    public Task(String name, String description, short complexity, short significance) {
        this.name = name;
        this.description = description;
        this.complexity = complexity;
        this.significance = significance;
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

    public short getComplexity() {
        return complexity;
    }

    public short getSignificance() {
        return significance;
    }

    public boolean isCompleted() {
        return completed;
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

    public void setComplexity(short complexity) {
        this.complexity = complexity;
    }

    public void setSignificance(short significance) {
        this.significance = significance;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
