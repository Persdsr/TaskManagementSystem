package io.github.persdsr.taskmanagementsystem.service;

import io.github.persdsr.taskmanagementsystem.entity.CommentEntity;
import io.github.persdsr.taskmanagementsystem.entity.TaskEntity;
import io.github.persdsr.taskmanagementsystem.entity.UserEntity;
import io.github.persdsr.taskmanagementsystem.exception.response.AuthorNotFoundException;
import io.github.persdsr.taskmanagementsystem.exception.response.TaskNotFoundException;
import io.github.persdsr.taskmanagementsystem.exception.response.UserNotFoundException;
import io.github.persdsr.taskmanagementsystem.model.TaskPriority;
import io.github.persdsr.taskmanagementsystem.model.TaskStatus;
import io.github.persdsr.taskmanagementsystem.model.dto.task.TaskDTO;
import io.github.persdsr.taskmanagementsystem.model.dto.task.TaskRequestDTO;
import io.github.persdsr.taskmanagementsystem.model.dto.user.CommentDTO;
import io.github.persdsr.taskmanagementsystem.model.request.CommentRequest;
import io.github.persdsr.taskmanagementsystem.repository.TaskRepo;
import io.github.persdsr.taskmanagementsystem.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;


    @PreAuthorize("hasRole('ADMIN')")
    public TaskDTO getTaskById(int id) {
        Optional<TaskEntity> task = taskRepo.findById(id);
        if (task.isPresent()) {
            return TaskDTO.toModel(task.get());
        } else {
            throw TaskNotFoundException.builder().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskDTO> getTasks(
            String author,
            String performer,
            TaskStatus status,
            TaskPriority priority,
            int pageNumber,
            int pageSize) {

        Specification<TaskEntity> specification = Specification.where(null);

        if (author != null) {
            specification = specification.and(TaskSpecifications.hasAuthor(author));
        }

        if (performer != null) {
            specification = specification.and(TaskSpecifications.hasPerformer(performer));
        }

        if (status != null) {
            specification = specification.and(TaskSpecifications.hasStatus(status));
        }

        if (priority != null) {
            specification = specification.and(TaskSpecifications.hasPriority(priority));
        }

        Page<TaskEntity> tasks = taskRepo.findAll(specification, PageRequest.of(pageNumber, pageSize));

        return tasks.stream()
                .map(TaskDTO::toModel)
                .toList();
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void addTask(TaskRequestDTO taskDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> author = userRepo.findByUsername(authentication.getName());
        TaskEntity task = TaskEntity.builder()
                .author(author.get())
                .title(taskDTO.getTitle())
                .status(TaskStatus.PENDING)
                .priority(taskDTO.getPriority())
                .description(taskDTO.getDescription()).build();

        taskRepo.save(task);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateTask(Map<String, Object> task, int id) {
        TaskEntity taskEntity = taskRepo.findById(id).orElse(null);

        if (taskEntity == null) {
            throw TaskNotFoundException.builder().build();
        }

        task.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(TaskEntity.class, key);
            field.setAccessible(true);

            if (field.getType().isEnum()) {
                value = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
            }

            ReflectionUtils.setField(field, taskEntity, value);
        });
        taskRepo.save(taskEntity);
    }



    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTask(int id) {
        TaskEntity task = taskRepo.findById(id).orElse(null);

        if (task == null) {
            throw TaskNotFoundException.builder().build();
        }

        taskRepo.delete(task);
    }

    @PreAuthorize("@permissionEvaluator.isPerformerOrAdmin(#id)")
    public void addCommentToTask(int id, CommentRequest comment) {
        TaskEntity task = taskRepo.findById(id).orElse(null);

        if (task == null) {
            throw TaskNotFoundException.builder().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CommentEntity commentEntity = CommentEntity.builder()
                .task(task)
                .text(comment.getText())
                .author(userRepo.findByUsername(authentication.getName()).get())
                .build();


        task.getComments().add(commentEntity);

        taskRepo.save(task);
    }

    public boolean isTaskAuthor(int id, String author) {
        Optional<TaskEntity> task = taskRepo.findById(id);

        if (task.isPresent()) {
            if (task.get().getAuthor().getUsername().equals(author)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw TaskNotFoundException.builder().build();
        }
    }

    public boolean isTaskPerformer(int id, String performer) {
        Optional<TaskEntity> task = taskRepo.findById(id);

        if (task.isPresent()) {
            if (task.get().getPerformer() != null && task.get().getPerformer().getUsername().equals(performer)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw TaskNotFoundException.builder().build();
        }
    }
}
