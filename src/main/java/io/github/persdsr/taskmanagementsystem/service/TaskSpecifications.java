package io.github.persdsr.taskmanagementsystem.service;

import io.github.persdsr.taskmanagementsystem.entity.TaskEntity;
import io.github.persdsr.taskmanagementsystem.model.TaskPriority;
import io.github.persdsr.taskmanagementsystem.model.TaskStatus;
import org.springframework.data.jpa.domain.Specification;


public class TaskSpecifications {

    public static Specification<TaskEntity> hasAuthor(String author) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author").get("username"), author);
    }

    public static Specification<TaskEntity> hasPerformer(String performer) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("performer").get("username"), performer);
    }

    public static Specification<TaskEntity> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<TaskEntity> hasPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }
}