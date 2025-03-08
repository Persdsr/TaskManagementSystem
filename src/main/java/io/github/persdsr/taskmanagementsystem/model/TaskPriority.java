package io.github.persdsr.taskmanagementsystem.model;

public enum TaskPriority {
    HIGH("Высокий"),
    MEDIUM("Средний"),
    LOW("Низкий");

    private final String priorityName;

    TaskPriority(String priorityName) {
        this.priorityName = priorityName;
    }
}
