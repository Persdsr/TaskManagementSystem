package io.github.persdsr.taskmanagementsystem.model.dto.task;

import io.github.persdsr.taskmanagementsystem.entity.TaskEntity;
import io.github.persdsr.taskmanagementsystem.model.dto.user.CommentDTO;
import lombok.Data;

import java.util.List;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private String author;
    private String performer;
    private String status;
    private String priority;
    private List<CommentDTO> comments;

    public static TaskDTO toModel(TaskEntity taskEntity) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(taskEntity.getTitle());
        taskDTO.setDescription(taskEntity.getDescription());
        taskDTO.setAuthor(taskEntity.getAuthor().getUsername());

        taskDTO.setStatus(taskEntity.getStatus().name());
        taskDTO.setPriority(taskEntity.getPriority().name());

        if (taskEntity.getPerformer() != null) {
            taskDTO.setPerformer(taskEntity.getPerformer().getUsername());
        }

        taskDTO.setComments(taskEntity.getComments()
                .stream()
                .map(CommentDTO::toModel)
                .toList()
        );
        return taskDTO;
    }
}
