package io.github.persdsr.taskmanagementsystem.model;

public enum TaskStatus {
    PENDING("В ожидании"),
    IN_PROGRESS("В прогрессе"),
    COMPLETED("Завершен"),;

    private final String statusName;

    TaskStatus(String statusName) {
        this.statusName = statusName;
    }


}
