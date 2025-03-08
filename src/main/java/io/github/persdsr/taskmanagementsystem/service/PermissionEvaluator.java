package io.github.persdsr.taskmanagementsystem.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class PermissionEvaluator {

    private final TaskService taskService;

    public PermissionEvaluator(TaskService taskService) {
        this.taskService = taskService;
    }

    public boolean isPerformerOrAdmin(int taskId) {
        return isTaskPerformer(taskId) || isAdmin();
    }

    public boolean isTaskAuthor(int taskId) {
        if (!isAuthenticated()) {
            return false;
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskService.isTaskAuthor(taskId, username);
    }

    public boolean isTaskPerformer(int taskId) {
        if (!isAuthenticated()) {
            return false;
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskService.isTaskPerformer(taskId, username);
    }

    public boolean isAdmin() {
        if (!isAuthenticated()) {
            return false;
        }
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}