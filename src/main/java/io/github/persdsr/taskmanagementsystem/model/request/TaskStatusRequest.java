package io.github.persdsr.taskmanagementsystem.model.request;

import io.github.persdsr.taskmanagementsystem.model.TaskStatus;

public class TaskStatusRequest {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}