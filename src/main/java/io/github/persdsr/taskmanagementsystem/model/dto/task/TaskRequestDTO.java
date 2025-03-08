package io.github.persdsr.taskmanagementsystem.model.dto.task;

import io.github.persdsr.taskmanagementsystem.model.TaskPriority;
import io.github.persdsr.taskmanagementsystem.model.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Nullable
    private String description;

    @Nullable
    private TaskPriority priority = TaskPriority.LOW;


}
