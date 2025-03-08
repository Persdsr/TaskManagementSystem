package io.github.persdsr.taskmanagementsystem.model.request;

import io.github.persdsr.taskmanagementsystem.model.TaskPriority;

public class TaskPriorityRequest {

    private TaskPriority priority;

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}
