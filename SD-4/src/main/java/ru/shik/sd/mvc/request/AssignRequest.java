package ru.shik.sd.mvc.request;

public class AssignRequest {

    int taskId;
    int checklistId;

    public AssignRequest() {
    }

    public AssignRequest(int taskId, int checklistId) {
        this.taskId = taskId;
        this.checklistId = checklistId;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getChecklistId() {
        return checklistId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setChecklistId(int checklistId) {
        this.checklistId = checklistId;
    }
}
