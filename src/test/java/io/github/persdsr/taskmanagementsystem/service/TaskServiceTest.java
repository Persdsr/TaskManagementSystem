package io.github.persdsr.taskmanagementsystem.service;

import io.github.persdsr.taskmanagementsystem.entity.TaskEntity;
import io.github.persdsr.taskmanagementsystem.entity.UserEntity;
import io.github.persdsr.taskmanagementsystem.exception.response.TaskNotFoundException;
import io.github.persdsr.taskmanagementsystem.model.TaskPriority;
import io.github.persdsr.taskmanagementsystem.model.TaskStatus;
import io.github.persdsr.taskmanagementsystem.model.dto.task.TaskDTO;
import io.github.persdsr.taskmanagementsystem.model.request.CommentRequest;
import io.github.persdsr.taskmanagementsystem.repository.TaskRepo;
import io.github.persdsr.taskmanagementsystem.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepo taskRepo;

    @Mock
    UserRepo userRepo;

    @InjectMocks
    TaskService taskService;

    @Mock
    private Authentication authentication;

    List<TaskEntity> tasks = new ArrayList<>();

    @BeforeEach()
    public void setUp() {
        String author = "John";
        String performer = "Alex";

        TaskEntity task1 = TaskEntity.builder()
                .id(1)
                .title("Task 1 title")
                .description("Task 1 description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .comments(List.of())
                .author(UserEntity.builder().username(author).build())
                .performer(UserEntity.builder().username(performer).build())
                .build();

        TaskEntity task2 = TaskEntity.builder()
                .id(2)
                .title("Task 2 title")
                .description("Task 2 description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .comments(List.of())
                .author(UserEntity.builder().username(author).build())
                .performer(UserEntity.builder().username(performer).build())
                .build();

        tasks.addAll(List.of(task1, task2));
    }

    @Test
    void testGetTaskById_TaskExists_ReturnsTaskDTO() {
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1)
                .title("Title")
                .description("description")
                .status(TaskStatus.PENDING)
                .performer(new UserEntity())
                .priority(TaskPriority.LOW)
                .comments(List.of())
                .author(new UserEntity())
                .build();


        when(taskRepo.findById(1)).thenReturn(Optional.of(taskEntity));

        TaskDTO result = taskService.getTaskById(1);

        assertEquals("Title", result.getTitle());
        verify(taskRepo, times(1)).findById(1);

    }

    @Test
    void testGetTaskById_TaskNotFound_ThrowsTaskNotFoundException() {
        when(taskRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1));
        verify(taskRepo, times(1)).findById(1);
    }


    @Test
    void getTasks_WithFilters_ReturnsFilteredTasks() {
        TaskStatus status = TaskStatus.IN_PROGRESS;
        TaskPriority priority = TaskPriority.HIGH;
        int pageNumber = 0;
        int pageSize = 5;
        String author = "John";
        String performer = "Alex";

        Page<TaskEntity> taskPage = new PageImpl<>(tasks);

        when(taskRepo.findAll(any(Specification.class), eq(PageRequest.of(pageNumber, pageSize)))).thenReturn(taskPage);

        List<TaskDTO> result = taskService.getTasks(author, performer, status, priority, pageNumber, pageSize);

        assertNotNull(result);
        assertEquals(2, result.size());

        TaskDTO firstTask = result.get(0);
        assertEquals("Task 1 title", firstTask.getTitle());
        assertEquals("Task 1 description", firstTask.getDescription());
        assertEquals("IN_PROGRESS", firstTask.getStatus());
        assertEquals("HIGH", firstTask.getPriority());
        assertEquals("John", firstTask.getAuthor());
        assertEquals("Alex", firstTask.getPerformer());

        verify(taskRepo, times(1))
                .findAll(any(Specification.class), eq(PageRequest.of(pageNumber, pageSize)));
    }

    @Test
    void testDeleteTask_TaskExists_TaskIsDeleted() {
        int taskId = 1;
        TaskEntity task = new TaskEntity();
        task.setId(taskId);

        when(taskRepo.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepo, times(1)).findById(taskId);
        verify(taskRepo, times(1)).delete(task);
    }

    @Test
    void testDeleteTask_TaskNotFound_ThrowsTaskNotFoundException() {
        int taskId = 1;
        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));
        verify(taskRepo, times(1)).findById(taskId);
        verify(taskRepo, never()).deleteById(any());
    }

    @Test
    void addCommentToTask_TaskExists_UserIsPerformerOrAdmin_CommentIsAdded() {
        int taskId = 1;
        String username = "John";
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("Test comment");

        TaskEntity task = new TaskEntity();
        task.setId(taskId);
        task.setPerformer(new UserEntity());

        UserEntity author = new UserEntity();
        author.setUsername(username);

        when(taskRepo.findById(taskId)).thenReturn(Optional.of(task));
        when(authentication.getName()).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(author));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        taskService.addCommentToTask(taskId, commentRequest);

        verify(taskRepo, times(1)).findById(taskId);
        verify(userRepo, times(1)).findByUsername(username);
        verify(taskRepo, times(1)).save(task);

        assertEquals(1, task.getComments().size());
        assertEquals("Test comment", task.getComments().get(0).getText());
        assertEquals(username, task.getComments().get(0).getAuthor().getUsername());
    }

    @Test
    void addCommentToTask_TaskNotFound_ThrowsTaskNotFoundException() {
        int taskId = 1;
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("Test comment");

        when(taskRepo.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.addCommentToTask(taskId, commentRequest));
        verify(taskRepo, times(1)).findById(taskId);
        verify(taskRepo, never()).save(any());
    }

}
